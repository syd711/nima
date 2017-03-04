package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.starsailor.actors.route.RoutePoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the path finding status
 * Component is not poolable since all are created once.
 */
public class RouteComponent implements Component {

  private List<RoutePoint> routePoints = new ArrayList<>();

  public List<RoutePoint> getRoutePoints() {
    return routePoints;
  }
}
