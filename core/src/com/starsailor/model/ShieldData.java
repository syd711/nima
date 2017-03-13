package com.starsailor.model;

import com.google.gson.annotations.Expose;

/**
 * Shield data
 */
public class ShieldData extends GameDataWithId<ShieldData> {
  @Expose
  private float health;
  @Expose
  private float damageAbsorptionFactor;

  @Expose
  private SpineData spineData;

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

  public SpineData getSpineData() {
    if(spineData == null) {
      return getParent().getSpineData();
    }
    return spineData;
  }

  public boolean isSpineDataExtended() {
    return spineData == null;
  }

  public void setSpineData(SpineData spineData) {
    this.spineData = spineData;
  }
}
