package com.nima.actors;

import com.badlogic.ashley.core.Entity;
import com.nima.components.ComponentFactory;
import com.nima.components.RouteComponent;
import com.nima.managers.EntityManager;
import com.nima.util.Resources;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a route
 */
public class Route extends Entity {

  public RouteComponent routeComponent;
  private List<NPC> ships = new ArrayList<>();

  public Route(String name) {
    routeComponent = ComponentFactory.addRouteComponent(this, name);
  }

  public List<NPC> getShips() {
    return ships;
  }

  public void spawnShip() {
    NPC m = new NPC(routeComponent.route, Resources.ACTOR_SPINE, Resources.ACTOR_DEFAULT_ANIMATION, 0.2f, 300, 300);
    ships.add(m);
    EntityManager.getInstance().add(m);
  }
}
