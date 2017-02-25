package com.starsailor.editor.util;

import com.starsailor.data.GameDataWithId;

/**
 *
 */
public class IdGenerator {
  private static IdGenerator instance = new IdGenerator();

  private int id;

  public static IdGenerator getInstance() {
    return instance;
  }

  public void update(GameDataWithId gameData) {
    if(id < gameData.getId()) {
      id = gameData.getId();
    }
  }

  public int createId() {
    id++;
    return id;
  }
}
