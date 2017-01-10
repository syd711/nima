package com.nima.actors;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.nima.components.ComponentFactory;
import com.nima.components.RouteComponent;
import com.nima.data.RouteProfile;
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

  public Route(RouteProfile routeProfile) {
    routeComponent = ComponentFactory.addRouteComponent(this, routeProfile);
    routeComponent.refreshSpawnOffset();
  }

  public List<NPC> getShips() {
    return ships;
  }

  public void spawnShip() {
    NPC m = new NPC(routeComponent.route, Resources.ACTOR_SPINE, Resources.ACTOR_DEFAULT_ANIMATION, 0.2f, 300, 300);
    m.scalingComponent.setCurrentValue(0.2f);
    m.scalingComponent.setTargetValue(1f);
    ships.add(m);
    EntityManager.getInstance().add(m);
    Gdx.app.log(getClass().getName(), "Route '" + routeComponent.route.name + "': spawned ship " + ships.size());
  }

}
