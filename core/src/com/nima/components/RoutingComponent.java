package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Contains the path finding status
 */
public class RoutingComponent implements Component, Pool.Poolable {

  private boolean active = false;

  public void update() {

  }

  @Override
  public void reset() {
    this.active = false;
  }
}
