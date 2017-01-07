package com.nima.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.nima.actors.Route;
import com.nima.components.RouteComponent;

/**
 * This system deals with the active routes.
 * Other systems ensure that the route entity is still in the engine,
 * so we only have to check the ship status on the route.
 */
public class RouteSystem extends AbstractIteratingSystem {
  public RouteSystem() {
    super(Family.all(RouteComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    Route route = (Route) entity;
    RouteComponent routeComponent = route.getComponent(RouteComponent.class);
    if(route.getShips().size() < routeComponent.route.shipCount) {
      route.spawnShip();
    }
  }
}