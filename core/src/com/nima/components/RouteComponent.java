package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.nima.data.RouteProfile;

/**
 * Contains the path finding status
 */
public class RouteComponent implements Component, Pool.Poolable {

  public RouteProfile route;
  public long lastSpawnTime;
  public long spawnOffset;

  @Override
  public void reset() {
    this.route = null;
    this.lastSpawnTime = 0;
    this.spawnOffset = 0;
  }

  public boolean isValidSpawnInterval() {
    return lastSpawnTime + route.minSpawnDelay + spawnOffset < System.currentTimeMillis();
  }
}
