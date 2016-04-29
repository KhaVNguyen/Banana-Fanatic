package com.mygdx.bananatic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by KhaNguyen on 4/17/15.
 * Banana class, like the Platform class, is also essentially a sprite object, but its moveDown() method makes it simpler and easier to update its position
 */
public class Banana {
    private TextureAtlas gameObjectsAtlas;
    private Sprite bananaSprite;

    public Banana(){
        gameObjectsAtlas = new TextureAtlas(Gdx.files.internal("SpriteImages/gameobjects.txt"));
        bananaSprite = new Sprite(gameObjectsAtlas.findRegion("banana"));
        bananaSprite.setY(Gdx.graphics.getHeight());
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

    public void draw(){
        bananaSprite.draw(Bananatic.batch);
    }

    public void dispose(){
        bananaSprite.getTexture().dispose();
        gameObjectsAtlas.dispose();
    }
}
