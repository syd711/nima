package com.starsailor.util;

import com.starsailor.data.JsonDataFactory;

import java.io.File;

/**
 * Persistable game settings
 */
public class GameSettings {
  private static File file = new File("gameSettings.json");

  private transient long playingTimeStart;

  public long playingTime;

  private GameSettings() {
    playingTimeStart = System.currentTimeMillis();
  }

  public static GameSettings load() {
    if(file.exists()) {
      GameSettings s = JsonDataFactory.loadDataEntity(file, GameSettings.class);
      if(s != null) {
        s.playingTimeStart = System.currentTimeMillis();
        return s;
      }
    }
    return new GameSettings();
  }

  public void save() {
    playingTime = playingTime + (System.currentTimeMillis() - playingTimeStart);
    JsonDataFactory.saveDataEntity(file, this);
  }
}
