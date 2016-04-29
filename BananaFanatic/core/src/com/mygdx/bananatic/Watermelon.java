package com.mygdx.bananatic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by KhaNguyen on 5/12/15.
 * Watermelon is also essentially a sprite object class that is similar to the Banana class, aside from its spawn behavior and effects upon being collected by a monkey
 */
public class Watermelon {
    private static TextureAtlas gameObjectsAtlas;
    private Sprite watermelonSprite;

    public Watermelon(){
        gameObjectsAtlas = new TextureAtlas(Gdx.files.internal("SpriteImages/gameobjects.txt"));
        watermelonSprite = new Sprite(Watermelon.gameObjectsAtlas.findRegion("watermelon"));
        watermelonSprite.setY(Gdx.graphics.getHeight());
    }

    public float getWidth(){
        return watermelonSprite.getWidth();
    }

    public float getHeight(){
        return watermelonSprite.getHeight();
    }

    public void setXPosition(float x){
        watermelonSprite.setX(x);
    }

    public void moveDown(){
        watermelonSprite.translateY(-175 * Gdx.graphics.getDeltaTime());
    }

    public float getXPosition(){
        return watermelonSprite.getX();
    }

    public float getYPosition(){
        return watermelonSprite.getY();
    }

    public void draw(){
        watermelonSprite.draw(Bananatic.batch);
    }

    public void dispose(){
        watermelonSprite.getTexture().dispose();
        gameObjectsAtlas.dispose();
    }


}
