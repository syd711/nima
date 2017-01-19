package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Stores the selection state
 */
public class SelectionComponent implements Component, Pool.Poolable {

  public boolean selected;

  @Override
  public void reset() {
    this.selected = false;
  }

  public void toggleSelection() {
    this.selected = !this.selected;
  }
}
