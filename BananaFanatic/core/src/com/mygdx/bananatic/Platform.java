package com.mygdx.bananatic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by KhaNguyen on 4/18/15.
 * Platform class is just a sprite object, but has its own class for simpler code to check if a monkey collides with it
 */
public class Platform {
    private Texture platformTexture;
    private Sprite platformSprite;

    public Platform(Texture t, float x, float y){
        platformTexture = t;
        platformSprite = new Sprite(platformTexture);
        platformSprite.setPosition(x, y);
    }

    public float getXPosition(){
        return platformSprite.getX();
    }

    public float getYPosition(){
        return platformSprite.getY();
    }

    public float getWidth(){
        return platformSprite.getWidth();
    }

    public float getHeight(){
        return platformSprite.getHeight();
    }

    public void draw(){
        Bananatic.batch.draw(platformSprite, getXPosition(), getYPosition());
    }

}
