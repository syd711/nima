package com.starsailor.data;

import com.starsailor.util.Resources;

import java.util.*;

/**
 * Access all data models stored in json format.
 */
public class DataEntities {
  /******** Entity Constants *******************************************/
  public static final String SPINE_CENTER_SLOT_NAME = "torso";

  public static final String SHIP_PLAYER = "player";

  /******** Entity Map *************************************************/
  private static Map<String, ShipProfile> ships = new HashMap<>();
  private static Map<String, WeaponProfile> weapons = new HashMap<>();
  private static Map<String, ShieldProfile> shields = new HashMap<>();

  static {
    ships = JsonDataFactory.createDataEntities(Resources.SHIP_PROFILES, ShipProfile.class);
    weapons = JsonDataFactory.createDataEntities(Resources.WEAPON_PROFILES, WeaponProfile.class);
    shields = JsonDataFactory.createDataEntities(Resources.SHIELD_PROFILES, ShieldProfile.class);

    for(ShipProfile shipProfile : ships.values()) {
      for(String weapon : shipProfile.weapons) {
        WeaponProfile weaponProfile = weapons.get(weapon);
        weaponProfile.type = WeaponProfile.Types.valueOf(weapon.toUpperCase());
        shipProfile.addWeaponProfile(weaponProfile);
      }

      if(shipProfile.shield != null) {
        ShieldProfile shieldProfile = shields.get(shipProfile.shield);
        shipProfile.shieldProfile = shieldProfile;
      }
    }
  }

  public static ShipProfile getShip(String profile) {
    return ships.get(profile);
  }
}
