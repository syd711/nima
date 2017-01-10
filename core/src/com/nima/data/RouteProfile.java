package com.nima.data;

import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A ship route used for path finding
 */
public class RouteProfile {
  public String name;
  public List<String> stations;
  public boolean circulating;
  public int shipCount;
  public long minSpawnDelay;
  public long spawnDelayOffset;

  public Map<String,Vector2> coordinates;

  public RouteProfile(String name) {
    this.name = name;
  }

  public void setCoordinates(String name, Vector2 centeredPosition) {
    if(coordinates == null) {
      coordinates = new HashMap<>();
    }
    coordinates.put(name, centeredPosition);
  }
}
