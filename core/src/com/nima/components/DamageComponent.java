package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Stores the damage
 */
public class DamageComponent implements Component, Pool.Poolable {

  public float health;
  public float currentDamage;

  @Override
  public void reset() {
    this.health = 0;
    this.currentDamage = 0;
  }
}
