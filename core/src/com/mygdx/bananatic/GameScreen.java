package com.mygdx.bananatic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;
import sun.jvm.hotspot.utilities.BitMap;
import java.util.Iterator;


/**
 * Created by Kha on 4/13/2015.
 */
public class GameScreen implements Screen{
    final Bananatic game;

    FreeTypeFontGenerator fontGenerator;

    BitmapFont timer; // shows visible timer at top of game screen, game ends at 100 seconds
    float timeLeft;

    Sprite backgroundImage;
    Sound bananaPickupSound;
    Music backgroundMusic;

    Monkey monkey1;
    Monkey monkey2;
    BitmapFont monkey1BananaCount;
    BitmapFont monkey2BananaCount;

    Array <Banana> bananaArray;
    long lastTimeBananaSpawned;
    Array <Pineapple> pineappleArray; // pineapples that get spawned and drop vertically
    Array <Watermelon> watermelonArray;

    Array <Platform> platformArray;

    public GameScreen(final Bananatic gam){
        game = gam;

        timeLeft = 120f;

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Banana Split.ttf"));
        timer = fontGenerator.generateFont(100);
        timer.setColor(Color.YELLOW);

        backgroundImage = new Sprite(new Texture(Gdx.files.internal("jungle_background.png")));
        monkey1 = new Monkey(game, Color.BLUE, 0, 0, 450f, 2100f, Keys.A, Keys.D, Keys.W, Keys.SHIFT_LEFT, 0);
        monkey2 = new Monkey(game, Color.RED, Gdx.graphics.getWidth() - 100, 0, 450f, 2100f, Keys.LEFT, Keys.RIGHT, Keys.UP, Keys.SHIFT_RIGHT, 0);

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("monkeys and bananas.ttf"));
        monkey1BananaCount = fontGenerator.generateFont(80);
        monkey1BananaCount.setColor(monkey1.getColor());
        monkey2BananaCount = fontGenerator.generateFont(80);
        monkey2BananaCount.setColor(monkey2.getColor());

        bananaArray = new Array <Banana>();
        addBananaToBananaList();

        pineappleArray = new Array <Pineapple>();

        watermelonArray = new Array <Watermelon>();

        platformArray = new Array <Platform>();
        platformArray.add(new Platform(new Texture(Gdx.files.internal("platform1.png")), monkey1.getWidth() + 5, 180));
        platformArray.add(new Platform(new Texture(Gdx.files.internal("platform2.png")), 550, 180));
        platformArray.add(new Platform(new Texture(Gdx.files.internal("platform1.png")), Gdx.graphics.getWidth() - 400, 180));
        platformArray.add(new Platform(new Texture(Gdx.files.internal("platform1.png")), -100, 420));
        platformArray.add(new Platform(new Texture(Gdx.files.internal("platform1.png")), Gdx.graphics.getWidth()/2 - 100, 420));
        platformArray.add(new Platform(new Texture(Gdx.files.internal("platform2.png")), Gdx.graphics.getWidth() - 200, 420));
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
    // LibGDX's render method handles both the updating of sprites' positions as well as drawing them to the screen in a single render method
    // some other frameworks divides up these tasks into two: "update" and "draw"
    public void render(float delta) {


            renderObjects();
            //renderBananas(); // render bananas that move vertically, falling from the sky
            //renderPineapples(); // render pineapples that move vertically, falling from the sky

            monkey1.update(platformArray); // given an array of platform from which the monkey will check collision
            monkey2.update(platformArray); // given an array of platform from which the monkey will check collision

            // check if any of the monkey's thrown bananas have collided with the opponent
            monkey1.updateThrownPineapples(monkey2);
            monkey2.updateThrownPineapples(monkey1);


            game.batch.begin(); // begin drawing sprites (after their positions have been adjusted)

            backgroundImage.draw(game.batch);
            for (Platform p : platformArray) {
                p.draw();
            }

            drawTimer();

            for (Banana b : bananaArray) {
                b.draw();
            }
            for (Pineapple p : pineappleArray) {
                p.draw();
            }
            for(Watermelon w: watermelonArray){
                w.draw();
            }

            monkey2.drawPineapples();
            monkey1.drawPineapples();

            if (monkey1.getNumberOfPineapplesCollected() == 1) {
                monkey1.drawPineapple(); //pineapple held, not yet thrown
            }

            if (monkey2.getNumberOfPineapplesCollected() == 1) {
                monkey2.drawPineapple();
            }

            monkey1.draw();
            monkey2.draw();


            drawBananaCount(monkey1, monkey1BananaCount, 25, 75);
            drawBananaCount(monkey2, monkey2BananaCount, Gdx.graphics.getWidth() - 75, 75);


            game.batch.end(); // end drawing sprites

            // System.out.println("The blue monkey's state is: " + monkey1.returnState() + " and is it grounded? : " + monkey1.isGrounded());

            if(timeLeft <= 0f){
                if(monkey1.getNumberOfBananasCollected() > monkey2.getNumberOfBananasCollected()){
                    game.setScreen(new BfEnd(game, monkey1));
                }
                else if (monkey2.getNumberOfBananasCollected() > monkey1.getNumberOfBananasCollected()){
                    game.setScreen(new BfEnd(game, monkey2));
                }
            }
    }

    // disposes of game assets after the game window is closed as to prevent memory leaks
    @Override
    public void dispose() {
        bananaPickupSound.dispose();
        backgroundMusic.dispose();
        monkey1.dispose();
        monkey2.dispose();
    }

    private void drawTimer(){
        timeLeft -= Gdx.graphics.getDeltaTime();
        int roundedElapsedTime = (int)timeLeft;
        String timeString = Integer.toString(roundedElapsedTime);
        timer.draw(game.batch, timeString, Gdx.graphics.getWidth() / 2 - 25, Gdx.graphics.getHeight() - 20);
    }


    // adds a new banana to the array of bananas with a random x position
    private void addBananaToBananaList(){
        Banana banana = new Banana();
        float randomXPosition = MathUtils.random(0, Gdx.graphics.getWidth() - banana.getWidth());
        banana.setXPosition(randomXPosition);
        bananaArray.add(banana);
        lastTimeBananaSpawned = TimeUtils.nanoTime();
    }

    private void drawBananaCount(Monkey monkey, BitmapFont monkeyBananaCount, float x, float y){
        String stringOfBananasCollected = Integer.toString(monkey.getNumberOfBananasCollected());
        monkeyBananaCount.draw(game.batch, stringOfBananasCollected, x, y);
    }

//    // renders bananas from the top of the screen, which descend down and disappear upon exiting the screen
//    private void renderBananas(){
//        if(TimeUtils.nanoTime() - lastTimeBananaSpawned > 300000000){ // nanoTime is the total amount of time the game has been running
//            addBananaToBananaList(); // spawns bananas every increment of time
//        }
//
//        Iterator<Banana> bananaIterator = bananaArray.iterator();
//        while(bananaIterator.hasNext()) { //iterate over each banana in the banana array and update its position
//            Banana banana = bananaIterator.next();
//            banana.moveDown();
//            if(monkey1.overlaps(banana)){ // banana is removed from the screen and banana list if collided with monkey
//                monkey1.addToBananaCounter(1);
//                banana.dispose();
//                bananaIterator.remove();
//            }
//            else if(monkey2.overlaps(banana)){
//                monkey2.addToBananaCounter(1);
//                banana.dispose();
//                bananaIterator.remove();
//
//            }
//            else if(banana.getYPosition() + banana.getHeight() <= 0) bananaIterator.remove();
//
//        }
//    }
//
//    public void addPineappleToPineappleList(){ // add pineapple to the list of pineapples spawned and on the screen
//        Pineapple pineapple = new Pineapple();
//        float randomXPosition = MathUtils.random(0, Gdx.graphics.getWidth() - pineapple.getWidth());
//        pineapple.setXPosition(randomXPosition);
//        pineappleArray.add(pineapple);
//
//    }
//
//    public void renderPineapples(){
//        int randomInt = MathUtils.random(1, 400);
//        if(randomInt == 1){ // 1 in 400 chance of occurring
//            addPineappleToPineappleList(); // add a pineapple with a random x value
//        }
//
//        Iterator<Pineapple> pineappleIterator = pineappleArray.iterator();
//        while(pineappleIterator.hasNext()) { //iterate over each pineapple in the pineapple array and update its position
//            Pineapple pineapple = pineappleIterator.next();
//            pineapple.moveDown();
//            if(monkey1.overlaps(pineapple)){ // pineapple is removed from the screen and pineapple list if collided with monkey, monkey's pineapple count increments by one
//                if(monkey1.getNumberOfPineapplesCollected() < 1) {
//                    Pineapple addedPineapple = new Pineapple();
//                    addedPineapple.setColor(monkey1.getColor());
//                    monkey1.addPineapple(addedPineapple);
//                    pineapple.dispose();
//                    pineappleIterator.remove();
//                }
//                // pineapple doesn't get removed from the screen if collided with a monkey that already is holding a pineapple
//            }
//            else if(monkey2.overlaps(pineapple)){
//                if(monkey2.getNumberOfPineapplesCollected() < 1) {
//                    Pineapple addedPineapple = new Pineapple();
//                    addedPineapple.setColor(monkey2.getColor());
//                    monkey2.addPineapple(addedPineapple);
//                    pineapple.dispose();
//                    pineappleIterator.remove();
//                }
//
//            }
//            else if(pineapple.getYPosition() + pineapple.getHeight() <= 0){
//                pineapple.dispose();
//                pineappleIterator.remove();
//            }
//
//        }
//    }

// add a new sprite object into its respective array to be drawn and updated
    public void addObjectToObjectList(Object object){
        if(object instanceof Banana){
            Banana b = (Banana)object;
            float randomXPosition = MathUtils.random(0, Gdx.graphics.getWidth() - b.getWidth());
            b.setXPosition(randomXPosition);
            bananaArray.add(b);
            lastTimeBananaSpawned = TimeUtils.nanoTime();
        }
        else if (object instanceof Pineapple){
            Pineapple p = (Pineapple)object;
            float randomXPosition = MathUtils.random(0, Gdx.graphics.getWidth() - p.getWidth());
            p.setXPosition(randomXPosition);
            pineappleArray.add(p);


        }

        else if (object instanceof Watermelon){
            Watermelon w =  (Watermelon)object;
            float randomXPosition = MathUtils.random(0, Gdx.graphics.getWidth() - w.getWidth());
            w.setXPosition(randomXPosition);
            watermelonArray.add(w);
        }
    }

    // moves the current sprites on screen (bananas, pineapples, watermelons, etc.) down to represent gravity and them falling
    public void updateObjectsPositions(String type){
        if(type.equals("Bananas")) {
            Iterator<Banana> bananaIterator = bananaArray.iterator();
            while (bananaIterator.hasNext()) { //iterate over each banana in the banana array and update its position
                Banana banana = bananaIterator.next();
                banana.moveDown();
                if (monkey1.overlaps(banana)) { // banana is removed from the screen and banana list if collided with monkey
                    monkey1.addToBananaCounter(1);
                    banana.dispose();
                    bananaIterator.remove();
                } else if (monkey2.overlaps(banana)) {
                    monkey2.addToBananaCounter(1);
                    banana.dispose();
                    bananaIterator.remove();

                } else if (banana.getYPosition() + banana.getHeight() <= 0) bananaIterator.remove();
            }
        }
        else if (type.equals("Pineapples")) {
            Iterator<Pineapple> pineappleIterator = pineappleArray.iterator();
            while (pineappleIterator.hasNext()) { //iterate over each pineapple in the pineapple array and update its position
                Pineapple pineapple = pineappleIterator.next();
                pineapple.moveDown();
                if (monkey1.overlaps(pineapple)) { // pineapple is removed from the screen and pineapple list if collided with monkey, monkey's pineapple count increments by one
                    if (monkey1.getNumberOfPineapplesCollected() < 1) {
                        Pineapple addedPineapple = new Pineapple();
                        addedPineapple.setColor(monkey1.getColor());
                        monkey1.addPineapple(addedPineapple);
                        pineapple.dispose();
                        pineappleIterator.remove();
                    }
                    // pineapple doesn't get removed from the screen if collided with a monkey that already is holding a pineapple
                } else if (monkey2.overlaps(pineapple)) {
                    if (monkey2.getNumberOfPineapplesCollected() < 1) {
                        Pineapple addedPineapple = new Pineapple();
                        addedPineapple.setColor(monkey2.getColor());
                        monkey2.addPineapple(addedPineapple);
                        pineapple.dispose();
                        pineappleIterator.remove();
                    }

                } else if (pineapple.getYPosition() + pineapple.getHeight() <= 0) {
                    pineapple.dispose();
                    pineappleIterator.remove();
                }

            }
        }
        else if (type.equals("Watermelons")){
            Iterator<Watermelon> watermelonIterator = watermelonArray.iterator();
            while(watermelonIterator.hasNext()) { //iterate over each pineapple in the pineapple array and update its position
                Watermelon watermelon = watermelonIterator.next();
                watermelon.moveDown();
                if(monkey1.overlaps(watermelon)){ // pineapple is removed from the screen and pineapple list if collided with monkey, monkey's pineapple count increments by one
                    watermelonIterator.remove();
                    monkey1.setIsBoosted(true);
                    monkey1.receiveWatermelonBoost();
                }
                else if(monkey2.overlaps(watermelon)) {
                    watermelonIterator.remove();
                    monkey2.setIsBoosted(true);
                    monkey2.receiveWatermelonBoost();
                }
                else if(watermelon.getYPosition() + watermelon.getHeight() <= 0){
                    watermelon.dispose();
                    watermelonIterator.remove();
                }

            }
        }

    }

    // render draws and then updates the sprites onto the screen
    public void renderObjects(){
        //banana spawn
        if(TimeUtils.nanoTime() - lastTimeBananaSpawned > 300000000){ // nanoTime is the total amount of time the game has been running
            Banana b = new Banana();
            addObjectToObjectList(b); // spawns bananas every increment of time
        }
        updateObjectsPositions("Bananas");

        int randomInt = MathUtils.random(1,1197); //change back to 800
        if(randomInt == 1){
            Watermelon w = new Watermelon();
            addObjectToObjectList(w);
        }
        updateObjectsPositions("Watermelons");


        // pineapple spawn
        randomInt = MathUtils.random(1, 400);
        if(randomInt == 1){ // 1 in 400 chance of occurring
            Pineapple p = new Pineapple();
            addObjectToObjectList(p);
        }
        updateObjectsPositions("Pineapples");
    }


}