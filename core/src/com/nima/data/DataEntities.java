package com.nima.data;

import com.nima.util.Resources;

import java.util.*;

/**
 * Created by Matthias on 10.01.2017.
 */
public class DataEntities {
  public static String WEAPON_LASER = "laser";

  private static Map<String, RouteProfile> routes = new HashMap<>();
  private static Map<String, ShipProfile> ships = new HashMap<>();
  private static Map<String, WeaponProfile> weapons = new HashMap<>();

  static {
    routes = JsonDataFactory.createDataEntities(Resources.ROUTE_PROFILES, RouteProfile.class);
    ships = JsonDataFactory.createDataEntities(Resources.SHIP_PROFILES, ShipProfile.class);
    weapons = JsonDataFactory.createDataEntities(Resources.WEAPON_PROFILES, WeaponProfile.class);
  }

  public static RouteProfile getRoute(String name) {
    return routes.get(name);
  }

  public static List<RouteProfile> getRoutesForLocation(String name) {
    List<RouteProfile> result = new ArrayList<>();
    for(RouteProfile routeProfile : routes.values()) {
      if(routeProfile.coordinates.containsKey(name)) {
        result.add(routeProfile);
      }
    }
    return result;
  }

  public static Collection<RouteProfile> getRoutes() {
    return routes.values();
  }

  public static WeaponProfile getWeapon(String weapon) {
    return weapons.get(weapon);
  }
}
