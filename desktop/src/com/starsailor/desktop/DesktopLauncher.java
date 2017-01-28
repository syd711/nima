package com.starsailor.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.starsailor.Game;
import com.starsailor.util.LogInterceptor;
import com.starsailor.util.Settings;

public class DesktopLauncher {
  public static void main(String[] arg) {
    LogInterceptor.interceptSystemOut("../../log.txt");

    Settings settings = Settings.getInstance();

    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.fullscreen = settings.fullscreen;
    config.title = "Starsailer (Version " + settings.version + ")";
    config.width = settings.width;
    config.height = settings.height;
    config.resizable = settings.resizable;
    config.backgroundFPS = settings.backgroundFPS;
    config.foregroundFPS = settings.foregroundFPS;

    if(settings.x != 0) {
      config.x = settings.x;
    }

    new LwjglApplication(new Game(), config);
  }
}
