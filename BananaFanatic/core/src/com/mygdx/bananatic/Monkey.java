package com.mygdx.bananatic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

/**
 * Created by Kha on 4/13/2015.
 * The barebones of the Monkey class is essentially a sprite image with position coordinates
 * Also included are methods to check interaction with objects, being able to instantly switch sprites based on user input (running, jumping, etc.),user interaction, etc.
 */
public class Monkey {
    final Bananatic game;

    final float GRAVITY_CONSTANT = 1300;

    public static final int ASCENDING = 1;
    public static final int DESCENDING = -1;
    public static final int FALLING = -2;
    public static final int STANDING = 0;

    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    private int jumpKey;
    private int leftKey;
    private int rightKey;
    private int throwKey; // key used to throw a pineapple, making it move the direction in which the monkey is facing

    private Sound throwSound;
    private Sound hitSound;

    private boolean grounded; // true if the monkey is standing on a surface such as the ground or a platform
    private boolean isOverlappingAPlatform;

    private Color color; // color that the monkey sprite will be tinted
    private TextureAtlas monkeyTextureAtlas; // libGDX optimized way of storing images grouped together, in this instance it is a sprite sheet of animation frames for the monkey
    private Sprite monkeySprite;

    private final int MAX_RUN_LEFT_FRAMES = 8; //max number of frames in the running left animation frames, loops through these to simulate movement
    private final int MAX_RUN_RIGHT_FRAMES = 8;
    private int currentRunRightFrame;
    private int currentRunLeftFrame;

    private final int MAX_ASCENDING_RIGHT_FRAMES = 4;
    private final int MAX_ASCENDING_LEFT_FRAMES = 4;
    private int currentAscendingRightFrame;
    private int currentAscendingLeftFrame;

    private final int MAX_DESCENDING_RIGHT_FRAMES = 8;
    private final int MAX_DESCENDING_LEFT_FRAMES = 8;
    private int currentDescendingRightFrame;
    private int currentDescendingLeftFrame;

    private float immobileDuration = 180; // game runs at 60 frames per second, so the monkey will be unable to move for 180 frames or 3 seconds

    private float xVelocity;
    private float yVelocity;
    private float originalYVelocity; // used in jump method to reset to after the jump is completed

    private int directionFacing;
    private int state;

    private int numberOfBananasCollected;

    private Array<Pineapple> pineapplesHeld;// pineapples collected by the monkey that can be thrown
    private Array<Pineapple> thrownPineapples; //pineapples that have already been thrown and are in the air

    private boolean isHit; // has had a pineapple thrown at it

    private boolean isBoosted;
    float boostTimeRemaining;

    public Monkey(final Bananatic game, int x, int y, float xv, float yv, int left, int right, int jump, int throwK, int bananas){
        this.game = game;
        int randomColorNumber = MathUtils.random(1, 7);
        switch(randomColorNumber){
            case 1:
                color = Color.YELLOW;
                break;
            case 2:
                color = Color.TEAL;
                break;
            case 3:
                color = Color.RED;
                break;
            case 4:
                color = Color.PINK;
                break;
            case 5:
                color = Color.ORANGE;
                break;
            case 6:
                color = Color.LIGHT_GRAY;
                break;
            case 7:
                color = Color.CYAN;
                break;
        }
        monkeyTextureAtlas = new TextureAtlas(Gdx.files.internal("SpriteImages/monkey_animations.txt"));
        monkeySprite = new Sprite(monkeyTextureAtlas.findRegion("monkey_idle_right"));
        monkeySprite.setPosition(x, y);
        currentRunLeftFrame = 0;
        currentRunRightFrame = 0;
        currentAscendingLeftFrame = 1;
        currentAscendingRightFrame = 1;
        xVelocity = xv;
        yVelocity = yv;
        originalYVelocity = yv;
        leftKey = left;
        rightKey = right;
        jumpKey = jump;
        throwKey = throwK;
        throwSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/objectThrownSound.mp3"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/hitSound.mp3"));
        numberOfBananasCollected = bananas;
        directionFacing = RIGHT;
        state = STANDING;
        grounded = true;
        pineapplesHeld = new Array<Pineapple>();
        thrownPineapples = new Array<Pineapple>();
        isHit = false;
        boostTimeRemaining = 600f;
        isBoosted = false;
    }


    public Color getColor(){
        return color;
    }

    public boolean isBoosted(){
        return isBoosted;
    }

    public void setIsBoosted(boolean flag){
        isBoosted = flag;
    }

    public float getXVelocity(){
        return xVelocity;
    }

    public float getyVelocity(){
        return yVelocity;
    }

    public void setXPosition(float x){
        monkeySprite.setX(x);
    }

    public float getXPosition(){
        return monkeySprite.getX();
    }

    public void setYPosition(float y){
        monkeySprite.setY(y);
    }

    public float getYPosition(){
        return monkeySprite.getY();
    }

    public void setPosition(float x, float y){
        monkeySprite.setX(x);
        monkeySprite.setY(y);
    }

    public float getWidth(){
        return monkeySprite.getWidth();
    }

    public float getHeight(){
        return monkeySprite.getHeight();
    }

    public void moveRight() {
        monkeySprite.translateX(xVelocity * Gdx.graphics.getDeltaTime());
    }

    public void moveLeft() {
        monkeySprite.translateX(-xVelocity * Gdx.graphics.getDeltaTime());
    }

    public void moveDown(float f) {
        monkeySprite.translateY(-f * Gdx.graphics.getDeltaTime());
    }


    public void addToBananaCounter(int i){
        numberOfBananasCollected += i;
    }


    //return what the monkey is currently doing
    public int returnState(){
        return state;
    }

    public void setState(int s){
        state = s;
    }

    public void setHit(boolean flag){
        isHit = flag;
    }

    public boolean isAscending(){
       if(state == ASCENDING){
           return true;
       }
       else{
           return false;
       }
    }

    public boolean isDescending(){
        if(state == DESCENDING){
            return true;
        }
        else{
            return false;
        }
    }

    //1st part of the jump action
    public void ascend(){
        //System.out.println("yVelocity: " + getYVelocity());
        //System.out.println("YPosition =   " + getYPosition());

        yVelocity -= 150; // as the monkey ascends, he will start fast then slow down near the peak of the jump
        setYPosition(getYPosition() + yVelocity * Gdx.graphics.getDeltaTime());
        if(yVelocity <= 0) { // has reached the top of the jump
            yVelocity = 0;
            setState(DESCENDING);
        }

    }

    //2nd part of the jump action
    public void descend(){
        //System.out.println("yVelocity: " + getYVelocity());
        //System.out.println("YPosition =   " + getYPosition());
        yVelocity += 150; // determines how fast the jump takes;
        setYPosition(getYPosition() - yVelocity * Gdx.graphics.getDeltaTime());
        if(yVelocity >= originalYVelocity){
            setState(STANDING);
        }

    }

    //move the monkey downwards a certain number of pixels (given in the gravity constant) each call of the render method as to simulate gravity
    public void applyGravity(float gravity){
        moveDown(gravity);
    }

    public boolean isGrounded(){
        return grounded;
    }

    public void setGrounded(boolean b){
        grounded = b;
    }

    public boolean isOverlappingAPlatform(){
        return isOverlappingAPlatform;
    }

    public void setOverlappingAPlatform(boolean b){
        isOverlappingAPlatform = b;
    }

    public void resetVelocity(){
        yVelocity = originalYVelocity;
    }


    public int getNumberOfBananasCollected(){
        return numberOfBananasCollected;
    }

    public void addPineapple(Pineapple pineapple){
        pineapplesHeld.add(pineapple);
    }

    public int getNumberOfPineapplesCollected(){
        return pineapplesHeld.size; //size of the pineapple array, ie. the number of pineapples the monkey has collected
    }

    // called when monkey faces to the left
    public void throwPineappleLeft(){
        throwSound.play();
        thrownPineapples.add(pineapplesHeld.get(pineapplesHeld.size - 1));
        thrownPineapples.get(thrownPineapples.size - 1).setPosition(getXPosition(), getYPosition() + getHeight() / 2); //set x and y positions of pineapple near the monkey throwing it
        thrownPineapples.get(thrownPineapples.size - 1).setMovingLeft();
        pineapplesHeld.removeIndex(pineapplesHeld.size - 1);
    }

    //called when monkey faces to the right
    public void throwPineappleRight(){
        throwSound.play();
        thrownPineapples.add(pineapplesHeld.get(pineapplesHeld.size - 1));
        thrownPineapples.get(thrownPineapples.size - 1).setPosition(getXPosition(), getYPosition() + getHeight()/2 ); //set x and y positions of pineapple near the monkey throwing it
        thrownPineapples.get(thrownPineapples.size - 1).setMovingRight();
        pineapplesHeld.removeIndex(pineapplesHeld.size - 1);
    }

    // @param: the other monkey (not the monkey calling the method)
    // if any of the pineapples collide with the other monkey, remove them from the array, then make it disappear
    // else: continue moving the pineapple
    public void updateThrownPineapples(Monkey otherMonkey){
        Iterator <Pineapple> pineappleIterator = thrownPineapples.iterator();
        while(pineappleIterator.hasNext()){
            Pineapple testedPineapple = pineappleIterator.next();
            if(testedPineapple.getXPosition() <= 0 || testedPineapple.getXPosition() >= Gdx.graphics.getWidth()){
                testedPineapple.dispose();
                pineappleIterator.remove();
            }
            else if(testedPineapple.overlapsMonkey(otherMonkey)){
                hitSound.play();
                otherMonkey.setHit(true);
                testedPineapple.dispose();
                pineappleIterator.remove();
            }
            else{
                testedPineapple.update();
            }
        }

    }

    //draw all the pineapples onto the screen that have been thrown, released from the monkey
    public void drawPineapples(){
        for(Pineapple p: thrownPineapples){
            p.draw();
        }

    }

    // receive a 10 second boost in horizontal velocity, running speed
    public void receiveWatermelonBoost(){
        boostTimeRemaining = 600f;
        if(color == Color.BLACK) {
            xVelocity = 800;
        }
        else{
            xVelocity = 700;
        }
    }

    /**
     * checks if the monkey is overlapping the banana by using cases in which
     * the monkey is not overlapping the banana as this is easier than checking when it is
     *
     * the monkey is overlapping when:
     *  its left edge is NOT to the right edge of the banana,
     *  its right edge is NOT to the left of the banana
     *  its bottom is NOT above the top of the banana,
     *  its top is NOT below the bottom of the banana
     *
     * as an example, if the monkey's left edge is to the right of the banana, then this would
     * cause the if-check to return true (ultimately false) and it would not fit the requirements
     * to be overlapping
     *
     *
     * @param b
     * @return true if any part of the monkeySprite overlaps any part of the banana
     */
    public boolean overlaps(Object o){
        if(o instanceof Banana){
            Banana tempBanana = (Banana)o;
            return !(getXPosition() > tempBanana.getXPosition() + tempBanana.getWidth() ||
                    getXPosition() + getWidth() < tempBanana.getXPosition() ||
                    getYPosition() > tempBanana.getYPosition() + tempBanana.getHeight() ||
                    getYPosition() + getHeight() < tempBanana.getYPosition());
        }

        else if (o instanceof Platform){
            Platform tempPlatform = (Platform)o;
            return !(getXPosition() > tempPlatform.getXPosition() + tempPlatform.getWidth() ||
                    getXPosition() + getWidth() < tempPlatform.getXPosition() ||
                    getYPosition() > tempPlatform.getYPosition() + tempPlatform.getHeight() ||
                    getYPosition() + getHeight() < tempPlatform.getYPosition());
        }

        else if (o instanceof Pineapple){
            Pineapple tempPineapple = (Pineapple)o;
            return !(getXPosition() > tempPineapple.getXPosition() + tempPineapple.getWidth() ||
                    getXPosition() + getWidth() < tempPineapple.getXPosition() ||
                    getYPosition() > tempPineapple.getYPosition() + tempPineapple.getHeight() ||
                    getYPosition() + getHeight() < tempPineapple.getYPosition());
        }

        else if (o instanceof Watermelon){
            Watermelon tempWatermelon = (Watermelon)o;
            return !(getXPosition() > tempWatermelon.getXPosition() + tempWatermelon.getWidth() ||
                    getXPosition() + getWidth() < tempWatermelon.getXPosition() ||
                    getYPosition() > tempWatermelon.getYPosition() + tempWatermelon.getHeight() ||
                    getYPosition() + getHeight() < tempWatermelon.getYPosition());
        }

        return false;

    }

    public void dispose(){
        monkeySprite.getTexture().dispose();
        monkeyTextureAtlas.dispose();
        throwSound.dispose();
    }

    // prevent monkey from going past the edges of the screen by resetting its position
    private void keepWithinScreenBounds(){
        if(getXPosition() + getWidth() >= Gdx.graphics.getWidth()){
            setXPosition(Gdx.graphics.getWidth() - getWidth());
        }
        else if(getXPosition() <= 0){
            setXPosition(0);
        }

        if(getYPosition() <= 0){
            setYPosition(0);
            setGrounded(true);
            setState(STANDING);
        }
    }

    // alters conditions of the monkey such as the direction it faces, the number of bananas it has collected, etc.
    // as a response to different events and user input
    public void update(Array<Platform> platformList){ // given an array of platform from which the monkey will check collision
        if(directionFacing == RIGHT){
            monkeySprite.setRegion(monkeyTextureAtlas.findRegion("monkey_idle_right"));
        }
        else if (directionFacing == LEFT){
            monkeySprite.setRegion(monkeyTextureAtlas.findRegion("monkey_idle_left"));
        }
        if(isHit) {
            color = Color.BLACK;
            boostTimeRemaining = 0f; //remove boost if monkey gets hit by pineapple
            monkeySprite.setRegion(monkeyTextureAtlas.findRegion("monkey_dead"));
            immobileDuration--;
        }
        if(immobileDuration <= 0) {
            isHit = false;
            immobileDuration = 180; //reset immobile duration after monkey becomes mobile again;
            monkeySprite.setRegion(monkeyTextureAtlas.findRegion("monkey_faceforward"));
        }

        if(isBoosted){
            boostTimeRemaining--;
            if(boostTimeRemaining <= 0f){
               // System.out.println("No longer boosted!");
                isBoosted = false;
                xVelocity = 450f; //reset velocity back to normal after boost time runs out
            }
        }

        if(Gdx.input.isKeyPressed(jumpKey) && isGrounded() && !isHit){ // jump - can't jump if hit or already in the air
            resetVelocity();
            setGrounded(false);
            setState(ASCENDING);
            currentAscendingLeftFrame = 1;
            currentAscendingRightFrame = 1;
        }
        else if (Gdx.input.isKeyPressed(rightKey) && returnState() != FALLING && !isHit){ //run right
            currentRunRightFrame++;
            if(currentRunRightFrame > MAX_RUN_RIGHT_FRAMES){
                currentRunRightFrame = 0;
            }
            else{
                monkeySprite.setRegion(monkeyTextureAtlas.findRegion(String.format("monkey_run_" + "%d" + "_right", currentRunRightFrame)));
            }
            directionFacing = RIGHT;
            moveRight();
        }
        else if(Gdx.input.isKeyPressed(leftKey) && returnState() != FALLING && !isHit){ //run left
            currentRunLeftFrame++;
            if(currentRunLeftFrame > MAX_RUN_LEFT_FRAMES){
                currentRunLeftFrame = 0;
            }
            else{
                monkeySprite.setRegion(monkeyTextureAtlas.findRegion(String.format("monkey_run_" + "%d" + "_left", currentRunLeftFrame)));
            }
            directionFacing = LEFT;
            moveLeft();
        }

        setOverlappingAPlatform(false); // guilty until proven innocent
        for(int i = 0; i < platformList.size; i++){
            if(returnState() != ASCENDING){
                if(overlaps(platformList.get(i))) { // if the monkey overlaps any of the platforms
                    setYPosition(platformList.get(i).getHeight() + platformList.get(i).getYPosition());
                    setOverlappingAPlatform(true);
                }
            }
        }

        if(isOverlappingAPlatform() == true){
            setState(STANDING);
            setGrounded(true);
        }

        else if(isOverlappingAPlatform() == false){
            setGrounded(false);
        }

        if(isAscending()){
            if(directionFacing == RIGHT){
                currentAscendingRightFrame++;
                if(currentAscendingRightFrame > MAX_ASCENDING_RIGHT_FRAMES){
                    currentAscendingRightFrame = MAX_ASCENDING_RIGHT_FRAMES;
                }
                monkeySprite.setRegion(monkeyTextureAtlas.findRegion(String.format("monkey_jump_" + "%d" +"_right", currentAscendingRightFrame)));
            }
            else if(directionFacing == LEFT){
                currentAscendingLeftFrame++;
                if(currentAscendingLeftFrame > MAX_ASCENDING_LEFT_FRAMES){
                    currentAscendingLeftFrame = MAX_ASCENDING_LEFT_FRAMES;
                }
                monkeySprite.setRegion(monkeyTextureAtlas.findRegion(String.format("monkey_jump_" + "%d" + "_left", currentAscendingLeftFrame)));
            }
            ascend();
        }
        else if(isDescending()){
            if(directionFacing == RIGHT){
                currentDescendingRightFrame++;
                if(currentDescendingRightFrame > MAX_DESCENDING_RIGHT_FRAMES){
                    currentDescendingRightFrame = MAX_DESCENDING_RIGHT_FRAMES;
                }
                monkeySprite.setRegion(monkeyTextureAtlas.findRegion(String.format("monkey_jump_" + "%d" +"_right", currentDescendingRightFrame)));
            }
            else if (directionFacing == LEFT){
                currentDescendingLeftFrame++;
                if(currentDescendingLeftFrame > MAX_DESCENDING_LEFT_FRAMES){
                    currentDescendingLeftFrame = MAX_DESCENDING_LEFT_FRAMES;
                }
                monkeySprite.setRegion(monkeyTextureAtlas.findRegion(String.format("monkey_jump_" + "%d" +"_left", currentDescendingLeftFrame)));
            }
            descend();
        }
        else if (!isDescending() && !isAscending() && !isGrounded()){
            setState(FALLING);
            applyGravity(GRAVITY_CONSTANT);
        }

        keepWithinScreenBounds();

        if(Gdx.input.isKeyPressed(throwKey) && !isHit){
            if(directionFacing == LEFT && pineapplesHeld.size != 0){
                throwPineappleLeft();

            }
            else if(directionFacing == RIGHT && pineapplesHeld.size != 0){
                throwPineappleRight();
            }
        }
    }

    public void draw(){
        monkeySprite.setColor(color);
        monkeySprite.draw(game.batch);
    }

    public void drawPineapple(){
        if(directionFacing == RIGHT){
            pineapplesHeld.get(0).setPosition(getXPosition() + getWidth()/2, getYPosition() + getHeight()/4); //draw pineapple to the side of the monkey depending on the direction it faces
        }
        else if(directionFacing == LEFT){
            pineapplesHeld.get(0).setPosition(getXPosition() - getWidth()/2, getYPosition() + getHeight()/4);
        }
        pineapplesHeld.get(0).draw();
    }

}