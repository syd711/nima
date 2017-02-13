package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Stores the health state
 */
public class HealthComponent implements Component, Pool.Poolable {
  public float maxHealth;
  public float health;

  @Override
  public void reset() {
    this.health = 100;
    this.maxHealth = 100;
  }

  public float getPercent() {
    return health * 100 / maxHealth;
  }
}
