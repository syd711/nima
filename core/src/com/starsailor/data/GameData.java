package com.starsailor.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Superclass for all game data entities
 */
public class GameData<T> {
  private transient boolean extendParentData;
  private transient T parent;

  private List<T> children = new ArrayList<>();

  public void setChildren(List<T> children) {
    this.children = children;
  }

  public List<T> getChildren() {
    return children;
  }

  public void addChild(T gameData) {
    children.add(gameData);
  }

  public boolean isExtendParentData() {
    return extendParentData;
  }

  public void setExtendParentData(boolean extendParentData) {
    this.extendParentData = extendParentData;
  }

  public T getParent() {
    return parent;
  }

  public void setParent(T parent) {
    this.parent = parent;
  }

}
