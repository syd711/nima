package com.nima.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.List;

import static com.nima.util.Resources.ROUTE_PROFILES;

/**
 * Creates the route profiles
 */
public class RouteProfiles {

  private static final String STATIONS = "stations";
  private static final String CIRCULATING = "circulating";

  private static List<RouteProfile> routes = new ArrayList<>();

  public static void loadRoutes() {
    int index = 0;
    while(true) {
      index++;
      String name = "route_" + index;
      RouteProfile profile = createProfile(name);
      if(profile != null) {
        routes.add(profile);
      }
      else {
        break;
      }
    }
    Gdx.app.log(RouteProfiles.class.toString(), "Loaded "  + routes.size() + " routes.");
  }

  public static RouteProfile createProfile(String name) {
    FileHandle file = Gdx.files.internal(ROUTE_PROFILES + name + ".json");
    if(!file.exists()) {
      return null;
    }

    String rawJson = file.readString();
    JsonReader jsonReader = new JsonReader();
    JsonValue root = jsonReader.parse(rawJson);
    boolean circulating = root.getBoolean(CIRCULATING);

    RouteProfile route = new RouteProfile(name);
    route.circulating = circulating;
    JsonValue stations = root.get(STATIONS);
    for(JsonValue fixture : stations) {
      route.stations.add(fixture.toString());
    }

    return route;
  }

  public static void setCoordinates(String name, Vector2 centeredPosition) {
    for(RouteProfile route : routes) {
      if(route.stations.contains(name)) {
        route.coordinates.put(name, centeredPosition);
        Gdx.app.log("Route Profiles", "Applied coordinates "
            + centeredPosition + " for location '" + name + "' on route '" + route.name + "'");
      }
    }
  }
}
