package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.nima.data.RouteProfile;
import com.nima.render.TiledMultiMapRenderer;
import com.nima.util.PolygonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the path finding status
 */
public class RoutingComponent implements Component, Pool.Poolable {

  public Vector2 target;
  public List<Vector2> targets;
  public boolean circulating = true;


  @Override
  public void reset() {
    target = null;
    targets = null;
  }

  /**
   * Applies the route data and returns the start location
   */
  public Vector2 applyRoute(RouteProfile route) {
    this.circulating = route.circulating;
    this.targets = new ArrayList<>(route.coordinates.values());
    this.target = targets.get(1);
    //return first location as start location
    Polygon clickPolygon = PolygonUtil.clickPolygon(target);
    TiledMultiMapRenderer.debugRenderer.render("route", clickPolygon);

    return targets.get(0);
  }
}
