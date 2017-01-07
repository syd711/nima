package com.nima.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nima.util.Resources.ROUTE_PROFILES;

/**
 * Creates the route profiles
 */
public class RouteProfiles {

  private static final String STATIONS = "stations";
  private static final String CIRCULATING = "circulating";
  public static final String SHIP_COUNT = "shipCount";
  public static final String SPAWN_DELAY_OFFSET = "spawnDelayOffset";
  public static final String MIN_SPAWN_DELAY = "minSpawnDelay";

  private static Map<String, RouteProfile> routes = new HashMap<>();

  public static void loadRoutes() {
    int index = 0;
    while(true) {
      index++;
      String name = "route_" + index;
      RouteProfile profile = createProfile(name);
      if(profile != null) {
        routes.put(name, profile);
      }
      else {
        break;
      }
    }
    Gdx.app.log(RouteProfiles.class.toString(), "Loaded " + routes.size() + " routes.");
  }

  public static RouteProfile createProfile(String name) {
    FileHandle file = Gdx.files.internal(ROUTE_PROFILES + name + ".json");
    if(!file.exists()) {
      return null;
    }

    String rawJson = file.readString();
    JsonReader jsonReader = new JsonReader();
    JsonValue root = jsonReader.parse(rawJson);

    RouteProfile routeProfile = new RouteProfile(name);
    routeProfile.circulating =  root.getBoolean(CIRCULATING);
    routeProfile.shipCount = root.getInt(SHIP_COUNT);
    routeProfile.spawnDelayOffset = root.getInt(SPAWN_DELAY_OFFSET);
    routeProfile.minSpawnDelay= root.getInt(MIN_SPAWN_DELAY);
    JsonValue stations = root.get(STATIONS);
    for(JsonValue fixture : stations) {
      routeProfile.stations.add(fixture.toString());
    }

    return routeProfile;
  }

  public static RouteProfile getRoute(String name) {
    return routes.get(name);
  }

  public static void setCoordinates(String name, Vector2 centeredPosition) {
    for(RouteProfile routeProfile : routes.values()) {
      if(routeProfile.stations.contains(name)) {
        routeProfile.coordinates.put(name, centeredPosition);
        Gdx.app.log("Route Profiles", "Applied coordinates "
            + centeredPosition + " for location '" + name + "' on route '" + routeProfile.name + "'");
      }
    }
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
}
