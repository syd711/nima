package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Stores the selection state
 */
public class SelectionComponent implements Component, Pool.Poolable {

  public boolean selected = false;

  @Override
  public void reset() {
    this.selected = false;
  }

  public void setSelected(boolean b) {
    this.selected = b;
  }
}
