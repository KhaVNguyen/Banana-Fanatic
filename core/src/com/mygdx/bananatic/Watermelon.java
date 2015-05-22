package com.mygdx.bananatic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by KhaNguyen on 5/12/15.
 */
public class Watermelon {
    private TextureAtlas gameObjectsAtlas;
    private Sprite watermelonSprite;

    public Watermelon(){
        gameObjectsAtlas = new TextureAtlas(Gdx.files.internal("gameobjects.txt"));
        watermelonSprite = new Sprite(gameObjectsAtlas.findRegion("watermelon"));
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
