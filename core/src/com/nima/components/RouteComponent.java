package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.nima.data.RouteProfile;

/**
 * Contains the path finding status
 */
public class RouteComponent implements Component, Pool.Poolable {

  public RouteProfile route;

  @Override
  public void reset() {
    this.route = null;
  }
}
