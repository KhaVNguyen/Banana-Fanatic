//The function of this class is to create  font and spritebatch files that can be accessed by multiple classes for efficiency
// It starts the game by entering through the main menu screen, then is later used to switch between screens depending on certain events such as button clicks
// It is accessed by the DesktopLauncher.
//accessed by the DesktopLauncher.

package com.mygdx.bananatic;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;

/**
 * Bananatic serves as the entry point for the game by displaying the main menu first. Later on, it is used to switch between different screens (main game screen, end screen, help menus, etc.)
 */

public class Bananatic extends Game {
	public static SpriteBatch batch;
	public BitmapFont font;


	@Override
	public void dispose() {
		font.dispose();
		batch.dispose();
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setScale(2);
		this.setScreen(new BfMainMenu(this)); //entry point of the game
	}
}
