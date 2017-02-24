package com.starsailor.data;

import com.google.gson.annotations.Expose;

/**
 * Shield data
 */
public class ShieldData extends GameData {
  @Expose
  private String name;
  @Expose
  private float rechargeTimeMillis;
  @Expose
  private float health;
  @Expose
  private float damageAbsorptionFactor;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public float getRechargeTimeMillis() {
    return rechargeTimeMillis;
  }

  public void setRechargeTimeMillis(float rechargeTimeMillis) {
    this.rechargeTimeMillis = rechargeTimeMillis;
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

  @Override
  public String toString() {
    return name;
  }
}
