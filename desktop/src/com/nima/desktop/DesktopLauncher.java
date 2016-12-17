package com.nima.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nima.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.fullscreen = false;
		config.title = "Nima 0.1";
		config.width = 1024;
		config.height = 768;
		config.resizable = false;
//		config.fullscreen = true;
		new LwjglApplication(new Main(), config);
	}
}
