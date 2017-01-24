package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class ShipDataComponent implements Component, Poolable {
  public float money = 0;
  public float health = 100;
  public float maxHealth = 100;
  public float shield = 0;
  public float maxShield = 0;

  @Override
  public void reset() {
    money = 0;
    health = 100;
    maxHealth = 100;
    shield = 0;
    maxShield = 0;
  }
}
