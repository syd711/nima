package com.starsailor.data;

/**
 * Contains all data of a weapon
 */
public class WeaponProfile {
  public float rechargeTimeMillis;
  public float damage;
  public float speed;
  public String name;

  @Override
  public String toString() {
    return "Weapon Profile '" + name + "'";
  }
}
