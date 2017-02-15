package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Just a marker interface
 */
public class GameEntityComponent implements Component, Pool.Poolable {
  @Override
  public void reset() {
    //nothing
  }
}
