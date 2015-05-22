package com.mygdx.bananatic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.bananatic.Bananatic;
import com.mygdx.bananatic.GameScreen;

public class BfMainMenu implements Screen {

    final Bananatic game;

    OrthographicCamera camera;

    public BfMainMenu(final Bananatic myGame) {
        game = myGame;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1440, 800);

    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, .7f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        //game.font.draw(game.batch, "Banana Fanatic ", 450, 300);
        //game.font.draw(game.batch, "HELP", 460, 100);
        //game.font.draw(game.batch, "START", 300, 100);

        Stage stage = new Stage(new ExtendViewport(1440, 800));

        Gdx.input.setInputProcessor(stage);

        LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = game.font;
        Label title = new Label("Banana Fanatic", labelStyle);
        title.setX(260);
        title.setY(400);

        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = game.font;
        TextButton help = new TextButton("Help", textButtonStyle);
        help.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Help Pressed");
                // game.setScreen(new BfHelp(game));
                dispose();
            }
        });
        help.setX(460);
        help.setY(100);

        TextButton start = new TextButton("Start", textButtonStyle);
        start.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Start Pressed");
                game.setScreen(new GameScreen(game));

            }
        });
        start.setX(300);
        start.setY(100);

        stage.addActor(help);
        stage.addActor(start);
        stage.addActor(title);

        stage.draw();

        Gdx.input.setInputProcessor(stage);

        game.batch.end();

        /*if (Gdx.input.isTouched()) {
            game.setScreen(new BfHelp(game));
            dispose();
        }*/
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

