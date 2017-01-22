package com.starsailor.data;

/**
 * Contains all data of a weapon
 */
public class WeaponProfile {
  public enum Types {
    LASER, MISSILE
  }

  public Types type;
  public float rechargeTimeMillis;
  public float damage;
  public float forceFactor;
  public float impactFactor;
  public String name;

  //custom fields depending on the weapon
  public float activationDistance;
  public float linearDamping;


  //steering if available
  public SteeringData steeringData;

  @Override
  public String toString() {
    return "Weapon Profile '" + name + "'";
  }
}
