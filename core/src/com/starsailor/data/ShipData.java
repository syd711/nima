package com.starsailor.data;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains all attributes a ship can have.
 */
public class ShipData extends GameData {
  public enum Types {
    MERCHANT, PIRATE, CRUSADER
  }

  @Expose
  public String name;
  public String spine;
  public float scale;
  public String defaultAnimation;

  //Behaviour attributes
  public float attackDistance;
  public float shootDistance;
  public float retreatDistance;
  public float formationDistance;

  public float health;

  public BodyData bodyData;

  //weapons
  public List<String> weapons = new ArrayList<>();
  public List<WeaponData> weaponDatas = new ArrayList<>();

  //shield
  public String shield;
  public ShieldData shieldData;

  //Steering
  public SteeringData steeringData;

  public ShipData.Types getType() {
    return Types.valueOf(name.toUpperCase());
  }

  public void addWeaponProfile(WeaponData profile) {
    if(weaponDatas == null) {
      weaponDatas = new ArrayList<>();
    }
    weaponDatas.add(profile);
  }

  @Override
  public String toString() {
    return "Ship Profile '" + spine + "'";
  }
}
