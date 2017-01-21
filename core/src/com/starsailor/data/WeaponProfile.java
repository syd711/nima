package com.starsailor.data;

/**
 * Contains all data of a weapon
 */
public class WeaponProfile {
  public float rechargeTimeMillis;
  public float damage;
  public float speed;
  public float impactFactor;
  public String name;
  public SteeringData steeringData;

  @Override
  public String toString() {
    return "Weapon Profile '" + name + "'";
  }
}
