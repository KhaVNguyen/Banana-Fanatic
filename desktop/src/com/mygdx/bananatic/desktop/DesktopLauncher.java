package com.mygdx.bananatic.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.bananatic.Bananatic;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Bananatic(), config);
		config.width = 1440;
		config.height = 900;
		config.vSyncEnabled = true;



	}
}
