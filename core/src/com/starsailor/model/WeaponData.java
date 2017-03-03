package com.starsailor.model;

import com.google.gson.annotations.Expose;

/**
 * Contains all data of a weapon
 */
public class WeaponData extends GameDataWithId<WeaponData> {

  public enum Types {
    LASER, MISSILE, PHASER, MINE, FLARES, ROCKET
  }

  public enum Category {
    PRIMARY, SECONDARY, DEFENSIVE, EMERGENCY
  }

  public WeaponData(int id, Types type) {
    super(id, null);
    this.type = type.name().toLowerCase();
  }

  @Expose
  private String type;
  @Expose
  private float rechargeTimeMillis;
  @Expose
  private float durationMillis; //for constant shooting
  @Expose
  private float damage;
  @Expose
  private float forceFactor;
  @Expose
  private float torque;
  @Expose
  private float impactFactor;

  //for bullets
  @Expose
  private int bulletCount = 1;
  @Expose
  private long bulletDelay = 0;

  //profile properties
  @Expose
  private String category;

  //audio,visual
  @Expose
  private String sound;
  @Expose
  private String collisionEffect;
  @Expose
  private String sprite;


  //custom fields depending on the weapon
  @Expose
  private float activationDistance;

  //steering if available
  @Expose
  private SteeringData steeringData;

  //box2d data
  @Expose
  private com.starsailor.model.BodyData bodyData;


  public float getRechargeTimeMillis() {
    return rechargeTimeMillis;
  }

  public void setRechargeTimeMillis(float rechargeTimeMillis) {
    this.rechargeTimeMillis = rechargeTimeMillis;
  }

  public float getDurationMillis() {
    return durationMillis;
  }

  public void setDurationMillis(float durationMillis) {
    this.durationMillis = durationMillis;
  }

  public float getDamage() {
    return damage;
  }

  public void setDamage(float damage) {
    this.damage = damage;
  }

  public float getForceFactor() {
    return forceFactor;
  }

  public void setForceFactor(float forceFactor) {
    this.forceFactor = forceFactor;
  }

  public float getTorque() {
    return torque;
  }

  public void setTorque(float torque) {
    this.torque = torque;
  }

  public float getImpactFactor() {
    return impactFactor;
  }

  public void setImpactFactor(float impactFactor) {
    this.impactFactor = impactFactor;
  }

  public int getBulletCount() {
    return bulletCount;
  }

  public void setBulletCount(int bulletCount) {
    this.bulletCount = bulletCount;
  }

  public long getBulletDelay() {
    return bulletDelay;
  }

  public void setBulletDelay(long bulletDelay) {
    this.bulletDelay = bulletDelay;
  }

  public String getSound() {
    return sound;
  }

  public void setSound(String sound) {
    this.sound = sound;
  }

  public String getCollisionEffect() {
    return collisionEffect;
  }

  public void setCollisionEffect(String collisionEffect) {
    this.collisionEffect = collisionEffect;
  }

  public String getSprite() {
    return sprite;
  }

  public void setSprite(String sprite) {
    this.sprite = sprite;
  }

  public float getActivationDistance() {
    return activationDistance;
  }

  public void setActivationDistance(float activationDistance) {
    this.activationDistance = activationDistance;
  }

  public SteeringData getSteeringData() {
    if(this.steeringData == null) {
      if(getParent() != null) {
        return getParent().getSteeringData();
      }
    }
    return steeringData;
  }

  public void setSteeringData(SteeringData steeringData) {
    this.steeringData = steeringData;
  }

  public com.starsailor.model.BodyData getBodyData() {
    if(this.bodyData == null) {
      if(getParent() != null) {
        return getParent().getBodyData();
      }
    }
    return bodyData;
  }

  public void setBodyData(com.starsailor.model.BodyData bodyData) {
    this.bodyData = bodyData;
  }

  public boolean isBodyDataExtended() {
    return bodyData == null;
  }

  public boolean isSteeringDataExtended() {
    return steeringData == null;
  }

  public String getCategory() {
    return category;
  }

  public Category getCategoryType() {
    return Category.valueOf(getCategory().toUpperCase());
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Types getWeaponType() {
    return Types.valueOf(getType().toUpperCase());
  }
}
