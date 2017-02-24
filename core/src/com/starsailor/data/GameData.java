package com.starsailor.data;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Superclass for all game data entities
 */
public class GameData {
  @Expose
  private boolean extendParentData;

  private List<GameData> children = new ArrayList<>();

  public List<GameData> getChildren() {
    return children;
  }

  public void addChild(GameData gameData) {
    children.add(gameData);
  }

  public boolean isExtendParentData() {
    return extendParentData;
  }

  public void setExtendParentData(boolean extendParentData) {
    this.extendParentData = extendParentData;
  }
}
