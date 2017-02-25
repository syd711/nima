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
      for(String weapon : shipData.getStatusData().getWeapons()) {
        WeaponData weaponData = weapons.get(weapon);
        weaponData.type = WeaponData.Types.valueOf(weapon.toUpperCase());
        shipData.getStatusData().addWeaponProfile(weaponData);
      }

      if(shipData.getStatusData().getShield() != null) {
        ShieldData shieldData = shields.get(shipData.getStatusData().getShield());
        shipData.getStatusData().setShieldData(shieldData);
      }
    }
  }

  public static ShipData getShip(String profile) {
    ShipData shipData = ships.get(profile);
    if(shipData == null) {
      throw new UnsupportedOperationException("No ship profile found for '" + profile + "'");
    }
    if(shipData.getDistanceData().getAttackDistance() <= 0) {
      throw new UnsupportedOperationException(profile + " does not define a attackDistance");
    }
    if(shipData.getDistanceData().getShootDistance() <= 0) {
      throw new UnsupportedOperationException(profile + " does not define a shootDistance");
    }
    if(shipData.getDistanceData().getRetreatDistance() <= 0) {
      throw new UnsupportedOperationException(profile + " does not define a retreatDistance");
    }
    return shipData;
  }
}
