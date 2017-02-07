package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.starsailor.actors.Fraction;

/**
 * The fraction component to determine the party of the ship
 */
public class FractionComponent implements Component, Pool.Poolable {

  public Fraction fraction;

  @Override
  public void reset() {
    fraction = null;
  }
}
