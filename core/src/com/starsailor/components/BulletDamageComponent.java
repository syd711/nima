package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class BulletDamageComponent implements Component, Poolable {
  public long damage = 0;

  @Override
  public void reset() {
    damage = 0;
  }
}
