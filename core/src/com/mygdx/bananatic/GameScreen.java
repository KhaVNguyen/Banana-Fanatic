package com.mygdx.bananatic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;


import java.util.Iterator;


/**
 * Created by Kha on 4/13/2015.
 */
public class GameScreen implements Screen{

    Sprite backgroundImage;
    Sound bananaPickupSound;
    Music backgroundMusic;
    Monkey blueMonkey;
    Monkey brownMonkey;
    Array <Banana> bananaArray;
    long lastTimeBananaSpawned;
    Array <Platform> platformArray;

    public GameScreen(){
        blueMonkey = new Monkey(new Texture(Gdx.files.internal("monkey_faceforward_blue.png")), 0, 0, 20f, 2000f, 0);
        brownMonkey = new Monkey(new Texture(Gdx.files.internal("monkey_faceforward.png")), Gdx.graphics.getWidth() - 100, 0, 20f, 2000f,0);
        backgroundImage = new Sprite(new Texture(Gdx.files.internal("jungle_background.png")));
        bananaArray = new Array<Banana>();
        addBananaToBananaList();

        platformArray = new Array<Platform>();
        platformArray.add(new Platform(new Texture(Gdx.files.internal("platform1_slim.png")), 50, 200));
        platformArray.add(new Platform(new Texture(Gdx.files.internal("platform2_slim.png")), 550, 200));
        platformArray.add(new Platform(new Texture(Gdx.files.internal("platform1_slim.png")), 1050, 200));

    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for(int i = 0; i < platformArray.size; i++){
            if(blueMonkey.returnState() == Monkey.DESCENDING){
                if(blueMonkey.overlapsPlatform(platformArray.get(i))){
                    blueMonkey.setState(Monkey.STANDING);
                }
            }
            if(blueMonkey.returnState() == Monkey.STANDING )
        }





        // handles user input via the keyboard
        // WASD controls one monkey, while the arrow keys control the other
        if(Gdx.input.isKeyPressed(Keys.W) && blueMonkey.returnState() == Monkey.STANDING){
            blueMonkey.resetVelocity();
            blueMonkey.setState(Monkey.ASCENDING);
        }

        if (Gdx.input.isKeyPressed(Keys.D)){
            blueMonkey.moveRight();
        }
        if(Gdx.input.isKeyPressed(Keys.A)){
            blueMonkey.moveLeft();
        }

        if(Gdx.input.isKeyPressed(Keys.UP) && brownMonkey.getYPosition() == 0){
            brownMonkey.setState(Monkey.ASCENDING);
        }
        if(Gdx.input.isKeyPressed(Keys.RIGHT)){
            brownMonkey.moveRight();
        }
        if(Gdx.input.isKeyPressed(Keys.LEFT)){
            brownMonkey.moveLeft();
        }



        monkeyJumpsIfKeyPressed(blueMonkey);

        monkeyJumpsIfKeyPressed(brownMonkey);



        // make sure the monkey sprites stay within the bounds of the screen
        blueMonkey.keepWithinScreenBounds();
        brownMonkey.keepWithinScreenBounds();


        if(TimeUtils.nanoTime() - lastTimeBananaSpawned > 1000000000){
            addBananaToBananaList();
        }

        Iterator<Banana> bananaIterator = bananaArray.iterator();
        while(bananaIterator.hasNext()) {
            Banana banana = bananaIterator.next();
            banana.moveDown();
            if(blueMonkey.overlapsBanana(banana)){
                blueMonkey.addToBananaCounter(1);
                banana.dispose();
                bananaIterator.remove();
                System.out.println("Blue Monkey banana counter: " + blueMonkey.getNumberOfBananasCollected());
            }
            else if(brownMonkey.overlapsBanana(banana)){
                brownMonkey.addToBananaCounter(1);
                banana.dispose();
                bananaIterator.remove();
                System.out.println("Yellow monkey banana counter: " + brownMonkey.getNumberOfBananasCollected());

            }
            else if(banana.getYPosition() <= 0) bananaIterator.remove();

        }


        Bananatic.batch.begin();

        backgroundImage.draw(Bananatic.batch);
        for(Platform p: platformArray){
            p.draw();
        }
        blueMonkey.draw(Bananatic.batch);
        brownMonkey.draw(Bananatic.batch);
        for(Banana b: bananaArray){
            b.draw(Bananatic.batch);
        }
        blueMonkey.drawBananaCount(Bananatic.batch);
        brownMonkey.drawBananaCount(Bananatic.batch);


        Bananatic.batch.end();

        System.out.println("The blue monkey's state is: " + blueMonkey.returnState());

    }

    @Override
    public void dispose() {
        bananaPickupSound.dispose();
        backgroundMusic.dispose();
        blueMonkey.dispose();
        brownMonkey.dispose();


    }

    private void monkeyJumpsIfKeyPressed(Monkey monkey){
        if(monkey.isAscending()){
            monkey.ascend();
        }
        else if(monkey.isDescending()){
            monkey.descend();
        }
    }

    private void addBananaToBananaList(){
        Banana banana = new Banana();
        float randomXPosition = MathUtils.random(0, Gdx.graphics.getWidth() - banana.getWidth());
        banana.setXPosition(randomXPosition);
        bananaArray.add(banana);
        lastTimeBananaSpawned = TimeUtils.nanoTime();
    }

    private void drawPlatforms(){
        for(Platform p: platformArray){
            p.draw();
        }
    }

    /** private boolean hitPlatform(Monkey m){
        for(int i = 0; i < platformArray.size; i++){
            if(m.returnState() == Monkey.DESCENDING){
                if(m.overlapsPlatform(platformArray.get(i))){
                    m.setState(Monkey.STANDING);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean offPlatform(Monkey m){
        for(int i = 0; i < platformArray.size; i++){
            if(!m.overlapsPlatform(platformArray.get(i))){
                return true;
            }
        }
        return false;
    } */




}
