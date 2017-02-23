package com.starsailor.data;

import com.starsailor.util.Resources;

import java.util.*;

/**
 * Access all data models stored in json format.
 */
@Deprecated
public class DataEntities {
  /******** Entity Constants *******************************************/
  public static final String SPINE_CENTER_SLOT_NAME = "torso";

  public static final String SHIP_PLAYER = "player";

  /******** Entity Map *************************************************/
  private static Map<String, ShipData> ships = new HashMap<>();
  private static Map<String, WeaponData> weapons = new HashMap<>();
  private static Map<String, ShieldData> shields = new HashMap<>();

  static {
    ships = JsonDataFactory.createDataEntities(Resources.SHIP_PROFILES, ShipData.class);
    weapons = JsonDataFactory.createDataEntities(Resources.WEAPON_PROFILES, WeaponData.class);
    shields = JsonDataFactory.createDataEntities(Resources.SHIELD_PROFILES, ShieldData.class);

    for(ShipData shipData : ships.values()) {
      for(String weapon : shipData.weapons) {
        WeaponData weaponData = weapons.get(weapon);
        weaponData.type = WeaponData.Types.valueOf(weapon.toUpperCase());
        shipData.addWeaponProfile(weaponData);
      }

      if(shipData.shield != null) {
        ShieldData shieldData = shields.get(shipData.shield);
        shipData.shieldData = shieldData;
      }
    }
  }

  public static ShipData getShip(String profile) {
    ShipData shipData = ships.get(profile);
    if(shipData == null) {
      throw new UnsupportedOperationException("No ship profile found for '" + profile + "'");
    }
    if(shipData.attackDistance <= 0) {
      throw new UnsupportedOperationException(profile + " does not define a attackDistance");
    }
    if(shipData.shootDistance <= 0) {
      throw new UnsupportedOperationException(profile + " does not define a shootDistance");
    }
    if(shipData.retreatDistance <= 0) {
      throw new UnsupportedOperationException(profile + " does not define a retreatDistance");
    }
    return shipData;
  }
}
