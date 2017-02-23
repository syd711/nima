package com.starsailor.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Superclass for all game data entities
 */
public class GameData {

  private List<GameData> children = new ArrayList<>();

  public List<GameData> getChildren() {
    return children;
  }

  public void addChild(GameData gameData) {
    children.add(gameData);
  }
}
