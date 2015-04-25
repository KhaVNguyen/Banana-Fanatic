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
        backgroundImage = new Sprite(new Texture(Gdx.files.internal("jungle_background.png")));
        blueMonkey = new Monkey(new Texture(Gdx.files.internal("monkey_faceforward_blue.png")), 0, 0, 450, 2100f, Keys.A, Keys.D, Keys.W, 0);
        brownMonkey = new Monkey(new Texture(Gdx.files.internal("monkey_faceforward_yellow.png")), Gdx.graphics.getWidth() - 100, 0, 450, 2100f, Keys.LEFT, Keys.RIGHT, Keys.UP, 0);
        bananaArray = new Array<Banana>();
        addBananaToBananaList();

        platformArray = new Array<Platform>();
        platformArray.add(new Platform(new Texture(Gdx.files.internal("platform1.png")), 50, 175));
        platformArray.add(new Platform(new Texture(Gdx.files.internal("platform2.png")), 550, 175));
        platformArray.add(new Platform(new Texture(Gdx.files.internal("platform1.png")), 1050, 175));
        platformArray.add(new Platform(new Texture(Gdx.files.internal("platform1.png")), 150, 400));
        platformArray.add(new Platform(new Texture(Gdx.files.internal("platform2.png")), 600, 400));
        platformArray.add(new Platform(new Texture(Gdx.files.internal("platform1.png")), 1050, 400));

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
        renderBananas();

        blueMonkey.update(platformArray); // given an array of platform from which the monkey will check collision
        brownMonkey.update(platformArray); // given an array of platform from which the monkey will check collision


        Bananatic.batch.begin(); // begin drawing sprites (after their positions have been adjusted)

        backgroundImage.draw(Bananatic.batch);
        for(Platform p: platformArray){
            p.draw();
        }
        for(Banana b: bananaArray){
            b.draw(Bananatic.batch);
        }
        blueMonkey.draw(Bananatic.batch);
        brownMonkey.draw(Bananatic.batch);
        blueMonkey.drawBananaCount(Bananatic.batch);
        brownMonkey.drawBananaCount(Bananatic.batch);


        Bananatic.batch.end(); // end drawing sprites

        // System.out.println("The blue monkey's state is: " + blueMonkey.returnState() + " and is it grounded? : " + blueMonkey.isGrounded());

    }

    // disposes of game assets after the game window is closed as to prevent memory leaks
    @Override
    public void dispose() {
        bananaPickupSound.dispose();
        backgroundMusic.dispose();
        blueMonkey.dispose();
        brownMonkey.dispose();
    }

    // adds a new banana to the array of bananas with a random x position
    private void addBananaToBananaList(){
        Banana banana = new Banana();
        float randomXPosition = MathUtils.random(0, Gdx.graphics.getWidth() - banana.getWidth());
        banana.setXPosition(randomXPosition);
        bananaArray.add(banana);
        lastTimeBananaSpawned = TimeUtils.nanoTime();
    }

    // renders bananas from the top of the screen, which descend down and disappear upon exiting the screen
    private void renderBananas(){
        if(TimeUtils.nanoTime() - lastTimeBananaSpawned > 1000000000){ // nanoTime is the total amount of time the game has been running
            addBananaToBananaList(); // spawns bananas every increment of time
        }

        Iterator<Banana> bananaIterator = bananaArray.iterator();
        while(bananaIterator.hasNext()) { //iterate over each banana in the banana array and update its position
            Banana banana = bananaIterator.next();
            banana.moveDown();
            if(blueMonkey.overlapsBanana(banana)){ // banana is removed from the screen and banana list if collided with monkey
                blueMonkey.addToBananaCounter(1);
                banana.dispose();
                bananaIterator.remove();
            }
            else if(brownMonkey.overlapsBanana(banana)){
                brownMonkey.addToBananaCounter(1);
                banana.dispose();
                bananaIterator.remove();

            }
            else if(banana.getYPosition() <= 0) bananaIterator.remove();

        }
    }

}