package com.mygdx.bananatic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by KhaNguyen on 4/17/15.
 */
public class Banana {
    private Texture bananaTexture;
    private Sprite bananaSprite;

    public Banana(){
        bananaTexture = new Texture(Gdx.files.internal("banana.png"));
        bananaSprite = new Sprite(bananaTexture);
        bananaSprite.setY(Gdx.graphics.getHeight() - bananaSprite.getHeight());
    }

    public Banana(float x, float y){
        super();
        bananaSprite.setPosition(x, y);
    }

    public float getWidth(){
        return bananaSprite.getWidth();
    }

    public float getHeight(){
        return bananaSprite.getHeight();
    }

    public void setXPosition(float x){
        bananaSprite.setX(x);
    }

    public void moveDown(){
        bananaSprite.translateY(-150f * Gdx.graphics.getDeltaTime());
    }

    public float getXPosition(){
        return bananaSprite.getX();
    }

    public float getYPosition(){
        return bananaSprite.getY();
    }

    public void draw(SpriteBatch sb){
        bananaSprite.draw(sb);
    }

    public void dispose(){
        bananaTexture.dispose();
    }



}
