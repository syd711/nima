package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.nima.data.RouteProfile;

import java.util.Collection;

/**
 * Contains the path finding status
 */
public class RoutingComponent implements Component, Pool.Poolable {

  public Vector2 target;
  public Collection<Vector2> targets;
  public boolean circulating = true;


  @Override
  public void reset() {
    target = null;
    targets = null;
  }

  public void applyRoute(RouteProfile route) {
    this.targets = route.coordinates.values();
  }
}
