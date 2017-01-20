package com.starsailor.data;

import com.starsailor.util.Resources;

import java.util.*;

/**
 * Access all data models stored in json format.
 */
public class DataEntities {
  /******** Entity Constants *******************************************/
  public static final String SPINE_CENTER_SLOT_NAME = "torso";
  public static final String WEAPON_LASER = "laser";
  public static final String WEAPON_MISSILE = "missile";

  public static final String SHIP_PLAYER = "player";
  public static final String SHIP_MERCHANT = "merchant";
  public static final String SHIP_PIRATE = "pirate";

  /******** Entity Map *************************************************/
  private static Map<String, ShipProfile> ships = new HashMap<>();
  private static Map<String, WeaponProfile> weapons = new HashMap<>();

  static {
    ships = JsonDataFactory.createDataEntities(Resources.SHIP_PROFILES, ShipProfile.class);
    weapons = JsonDataFactory.createDataEntities(Resources.WEAPON_PROFILES, WeaponProfile.class);

    for(ShipProfile shipProfile : ships.values()) {
      for(String weapon : shipProfile.weapons) {
        WeaponProfile weaponProfile = weapons.get(weapon);
        shipProfile.addWeaponProfile(weaponProfile);
      }
    }
  }

  public static WeaponProfile getWeapon(String weapon) {
    return weapons.get(weapon);
  }

  public static ShipProfile getShip(String profile) {
    return ships.get(profile);
  }
}
