package com.starsailor.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.starsailor.Game;
import com.starsailor.util.Settings;

public class DesktopLauncher {
  public static void main(String[] arg) {
//    LogInterceptor.interceptSystemOut("../../log.txt");

    Settings settings = Settings.getInstance();

    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.fullscreen = settings.fullscreen;
    config.title = "Starsailer (Version " + settings.version + ")";
    config.width = settings.width;
    config.height = settings.height;
    config.resizable = settings.resizable;
    config.backgroundFPS = settings.backgroundFPS;
    config.foregroundFPS = settings.foregroundFPS;
    //config.addIcon("ico/favicon-32x32.png", Files.FileType.Internal);

    if(settings.x != 0) {
      config.x = settings.x;
    }

//    new LwjglApplication(new Box2dTestApplication(), config);
    new LwjglApplication(new Game(), config);
  }
}
