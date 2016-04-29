package com.mygdx.bananatic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


/**
 * Created by Kha Nguyen on 4/29/15.
 */
public class Pineapple {
    private TextureAtlas gameObjectsAtlas;
    private TextureRegion pineappleTextureRegion;
    private Sprite pineappleSprite;

    private boolean isMovingLeft;
    private boolean isMovingRight;

    public Pineapple(){
        gameObjectsAtlas = new TextureAtlas("SpriteImages/gameobjects.txt");
        pineappleTextureRegion = gameObjectsAtlas.findRegion("pineapple");
        pineappleSprite = new Sprite(pineappleTextureRegion);
        pineappleSprite.setY(Gdx.graphics.getHeight());
        isMovingLeft = false;
        isMovingRight = false;
    }

    public void setColor(Color color){
        pineappleSprite.setColor(color);

    }

    public float getWidth(){
        return pineappleSprite.getWidth();
    }

    public float getHeight(){
        return pineappleSprite.getHeight();
    }

    public void setXPosition(float x){
        pineappleSprite.setX(x);
    }

    public void setPosition(float x, float y){
        pineappleSprite.setPosition(x, y);
    }

    public void moveDown(){
        pineappleSprite.translateY(-150f * Gdx.graphics.getDeltaTime());
    }

    public float getXPosition(){
        return pineappleSprite.getX();
    }

    public float getYPosition(){
        return pineappleSprite.getY();
    }

    public void setMovingLeft(){
        isMovingLeft = true;
        isMovingRight = false;
    }

    public void setMovingRight(){
        isMovingRight = true;
        isMovingLeft = false;
    }

    public void moveLeft(){
        setXPosition(getXPosition() - (800 * Gdx.graphics.getDeltaTime()));
    }

    public void moveRight(){
        setXPosition(getXPosition() + (800 * Gdx.graphics.getDeltaTime()));
    }

    // returns true if detected collision with a monkey
    public boolean overlapsMonkey(Monkey monkey){
        return !(getXPosition() > monkey.getXPosition() + monkey.getWidth() ||
                getXPosition() + getWidth() < monkey.getXPosition() ||
                getYPosition() > monkey.getYPosition() + monkey.getHeight() ||
                getYPosition() + getHeight() < monkey.getYPosition());
    }

    // called repeatedly on each pineapple to move them depending the direction it was thrown
    public void update(){
        if(isMovingLeft){
            moveLeft();
        }
        else if (isMovingRight){
            moveRight();
        }
    }



    public void draw(){
        pineappleSprite.draw(Bananatic.batch);
    }

    public void dispose(){
        pineappleSprite.getTexture().dispose();
        gameObjectsAtlas.dispose();
    }
}
