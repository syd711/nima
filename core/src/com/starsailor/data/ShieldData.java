package com.starsailor.data;

import com.google.gson.annotations.Expose;

/**
 * Shield data
 */
public class ShieldData extends GameDataWithId<ShieldData> {
  @Expose
  private float health;
  @Expose
  private float damageAbsorptionFactor;

  public ShieldData(int id, ShieldData shieldData) {
    super(id, shieldData.getName());
    this.health = shieldData.getHealth();
    this.damageAbsorptionFactor = shieldData.getDamageAbsorptionFactor();
  }

  public float getHealth() {
    return health;
  }

  public void setHealth(float health) {
    this.health = health;
  }

  public float getDamageAbsorptionFactor() {
    return damageAbsorptionFactor;
  }

  public void setDamageAbsorptionFactor(float damageAbsorptionFactor) {
    this.damageAbsorptionFactor = damageAbsorptionFactor;
  }
}
