package com.nima.actors;

import com.badlogic.ashley.core.Entity;
import com.nima.components.ComponentFactory;
import com.nima.components.RouteComponent;

/**
 * Represents a route
 */
public class Route extends Entity {

  public RouteComponent routeComponent;

  public Route(String name) {
    routeComponent = ComponentFactory.addRouteComponent(this, name);
  }
}
