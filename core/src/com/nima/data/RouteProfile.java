package com.nima.data;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * A ship route used for path finding
 */
public class RouteProfile {
  public String name;
  public String shipType;
  public List<Vector2> routeCoordinates = new ArrayList<>();
  public long minSpawnDelay;
  public long spawnDelayOffset;
  public Vector2 spawnPoint;

  public RouteProfile(String name) {
    this.name = name;
  }
}
