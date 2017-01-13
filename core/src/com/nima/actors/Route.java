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

  public void spawnShip() {
//    NPC m = new NPC(routeComponent.route, DataEntities.getShip(DataEntities.SHIP_MERCHANT), 300, 300);
//    m.scalingComponent.setCurrentValue(0.2f);
//    m.scalingComponent.setTargetValue(1f);
//    ships.add(m);
//    EntityManager.getInstance().add(m);
//    Gdx.app.log(getClass().getName(), "Route '" + routeComponent.route.name + "': spawned ship " + ships.size());
  }

}
