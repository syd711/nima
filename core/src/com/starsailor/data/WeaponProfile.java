package com.starsailor.data;

/**
 * Contains all data of a weapon
 */
public class WeaponProfile {
  public enum Types {
    LASER, MISSILE, PHASER, MINE, FLARES, ROCKET
  }

  public enum Category {
    PRIMARY, SECONDARY, DEFENSIVE, EMERGENCY
  }

  public Types type;
  public float rechargeTimeMillis;
  public float durationMillis; //for constant shooting
  public float damage;
  public float forceFactor;
  public float torque;
  public float impactFactor;

  //for bullets
  public int bulletCount = 1;
  public long bulletDelay = 0;

  public String name;
  public String sound;
  public String category;

  //custom fields depending on the weapon
  public float activationDistance;

  //steering if available
  public SteeringData steeringData;

  //box2d data
  public BodyData bodyData;

  public Category getCategory() {
    return Category.valueOf(category.toUpperCase());
  }

  @Override
  public String toString() {
    return "Weapon Profile '" + name + "'";
  }
}
