//The function of this class is to create the 
//help screen and to create the next and home buttons.
//BfHelp is accessed by BfMainMenu when the help button
//is clicked.
package com.mygdx.bananatic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class BfHelp implements Screen {

    final Bananatic game;
    String[] helpScreens = {"HelpScreen/Slide1.jpg",
            "HelpScreen/Slide2.jpg",
            "HelpScreen/Slide3.jpg",
            "HelpScreen/Slide4.jpg",
            "HelpScreen/Slide5.jpg",
            "HelpScreen/Slide6.jpg",
            "HelpScreen/Slide7.jpg",};
    // index for the helpScreens array
    int idx = 0;
    OrthographicCamera camera;
    Stage stage;

    public BfHelp(final Bananatic myGame) {
        game = myGame;
        stage = new Stage(new ExtendViewport(1280, 720));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0.5f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //needed to handle click event 
        Gdx.input.setInputProcessor(stage);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        //makes the background image
        Texture texture = new Texture(Gdx.files.internal(helpScreens[idx]));
        game.batch.draw(texture, 0, 0);

        //makes the next and home buttons
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = game.font;

        TextButton next = new TextButton("Next", textButtonStyle);
        //displays next help screen when next button is pressed
        next.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //System.out.println("Next Pressed");
                //goes through the help slides and when the 
                //last one is displayed, it goes to the first one
                if (idx < 6) {
                    idx++;
                }
                else {
                    idx = 0;
                }
                Texture texture = new Texture(Gdx.files.internal(helpScreens[idx]));
                game.batch.begin();
                game.batch.draw(texture, 0, 0);
                game.batch.end();
            }
        });
        next.setX(600);
        next.setY(110);
        stage.addActor(next);

        textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = game.font;
        TextButton home = new TextButton("Home", textButtonStyle);
        //when the home button gets pressed, go back to the main screen
        home.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Home Pressed");
                game.setScreen(new BfMainMenu(game));
                dispose();
            }
        });
        home.setX(860);
        home.setY(110);
        stage.addActor(home);

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