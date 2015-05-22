package com.mygdx.bananatic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mygdx.bananatic.Bananatic;
import com.mygdx.bananatic.BfMainMenu;
import com.mygdx.bananatic.Monkey;

public class BfEnd implements Screen {

    final Bananatic game;
    Monkey monkey;

    OrthographicCamera camera;

    public BfEnd(final Bananatic myGame, Monkey myMonkey) {
        game = myGame;
        monkey = myMonkey;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1440, 900);

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

        game.font.draw(game.batch, "Winner!", Gdx.graphics.getWidth() / 2 - monkey.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        monkey.setPosition(Gdx.graphics.getWidth()/2 - monkey.getWidth()/2, Gdx.graphics.getHeight()/2 - monkey.getHeight() - 50);
        monkey.draw();
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new BfMainMenu(game));
            dispose();
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
        // TODO Auto-generated method stub

    }



}

