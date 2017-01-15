package com.nima.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains all attributes a ship can have.
 */
public class ShipProfile {
  public String spine;
  public float scale;
  public String defaultAnimation;

  //Behaviour attributes
  public float attackDistance;

  //weapons
  public List<String> weapons;
  public List<WeaponProfile> weaponProfiles;

  //Speed
  public float maxSpeed;
  public float rotationSpeed;
  public float increaseSpeed;
  public float decreaseSpeed;

  //Steering
  public float boundingRadius;
  public float maxLinearSpeed;
  public float maxLinearAcceleration;
  public float maxAngularSpeed;
  public float maxAngularAcceleration;

  public void addWeaponProfile(WeaponProfile profile) {
    if(weaponProfiles == null) {
      weaponProfiles = new ArrayList<>();
    }
    weaponProfiles.add(profile);
  }

  @Override
  public String toString() {
    return "Ship Profile '" + spine + "'";
  }
}
