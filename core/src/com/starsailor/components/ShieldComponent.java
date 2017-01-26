package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;
import com.starsailor.Game;

/**
 *
 */
public class ShieldComponent implements Component, Pool.Poolable {
  public float maxHealth;
  public float health;
  public float rechargeTimeMillis;
  public float damageAbsorptionFactor;

  private long lastIgnitionTime = 0;
  private boolean active = false;

  public Body body;

  @Override
  public void reset() {
    this.maxHealth = 0;
    this.health = 0;
  }

  public boolean isCharged() {
    float current = Game.currentTimeMillis - lastIgnitionTime;
    return current > rechargeTimeMillis;
  }

  public boolean isActive() {
    return active;
  }

  public float applyDamage(float damage) {
    this.health = health - (damage * damageAbsorptionFactor);
    if(this.health <= 0) {
      this.active = false;
      //normalize remaining damage value again
      return Math.abs(health/damageAbsorptionFactor);
    }
    return 0;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public boolean isRemaining() {
    return health > 0;
  }
}
