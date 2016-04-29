package com.mygdx.bananatic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.bananatic.Bananatic;
import com.mygdx.bananatic.BfMainMenu;
import com.mygdx.bananatic.Monkey;

public class BfEnd implements Screen {

    final Bananatic game;
    Sprite backgroundImage;
    Monkey monkey;
    Music applause;

    OrthographicCamera camera;

    public BfEnd(final Bananatic myGame, Monkey myMonkey) {
        game = myGame;
        backgroundImage = new Sprite(new Texture(Gdx.files.internal("SpriteImages/jungle_background.png")), 0, 0);
        monkey = myMonkey;
        applause = Gdx.audio.newMusic(Gdx.files.internal("Sounds/clapping.mp3"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        applause.play();
        applause.setLooping(true);
        game.batch.draw(backgroundImage.getTexture(),0,0);
        game.font.draw(game.batch, "Winner!", Gdx.graphics.getWidth() / 2 - monkey.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        game.font.draw(game.batch, "Click anywhere to return to the main menu.",50, 50);
        monkey.setPosition(Gdx.graphics.getWidth()/2 - monkey.getWidth()/2, Gdx.graphics.getHeight()/2 - monkey.getHeight() - 50);
        monkey.draw();
        game.batch.end();

        if (Gdx.input.isTouched()) {
        	applause.stop();
            game.setScreen(new BfMainMenu(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        applause.dispose();
        backgroundImage.getTexture().dispose();
        // TODO Auto-generated method stub

    }



}

