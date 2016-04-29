//The function of this class is to create the main menu 
//and to create the help and start buttons. BfMainMenu is
//accessed by the BananaFanatic class.
package com.mygdx.bananatic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;


public class BfMainMenu implements Screen {

    final Bananatic game;

    OrthographicCamera camera;
    Stage stage;
    boolean gameStartPressed; 

    public BfMainMenu(final Bananatic myGame) {
        game = myGame;
        stage = new Stage(new ExtendViewport(1280, 720));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
        gameStartPressed = false;
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub  
    }

    @Override
    public void render(float delta) {
        //creates background
        Gdx.gl.glClearColor(0.2f, .7f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //needed to handle click event
        Gdx.input.setInputProcessor(stage);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        //makes the background image
        Texture texture = new Texture(Gdx.files.internal("HelpScreen/Slide9.jpg"));
        game.batch.draw(texture, 0, 0);

        //makes the help and start buttons
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = game.font;

        TextButton help = new TextButton("Help", textButtonStyle);
        //displays the help screen when help button is pressed
        help.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //System.out.println("Help Pressed");
                game.setScreen(new BfHelp(game));
            }
        });
        help.setX(Gdx.graphics.getWidth()/2 + 100);
        help.setY(110);

        TextButton start = new TextButton("Start", textButtonStyle);
        start.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //System.out.println("Start Pressed");
            	if(gameStartPressed == false)
                game.setScreen(new GameScreen(game));
            	gameStartPressed = true;


            }
        });
        start.setX(Gdx.graphics.getWidth()/2 - 100);
        start.setY(110);

        stage.addActor(help);
        stage.addActor(start);

        game.batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();


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
        stage.dispose();
        // TODO Auto-generated method stub


    }



}