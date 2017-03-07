package com.starsailor.actors.route;

import com.starsailor.actors.FormationOwner;
import com.starsailor.actors.GameEntity;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.RouteComponent;
import com.starsailor.managers.EntityManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a route
 */
public class Route extends GameEntity {
  public RouteComponent routeComponent;

  private String name;

  private Map<Integer, FormationOwner> routeFormationOwners = new HashMap<>();

  public Route(String name) {
    this.name = name;
    routeComponent = ComponentFactory.addRouteComponent(this);
  }

  public FormationOwner getOrCreateFormationMember(int routeIndex) {
    if(routeFormationOwners.containsKey(routeIndex)) {
      return routeFormationOwners.get(routeIndex);
    }

    FormationOwner formationOwner = new FormationOwner(this, routeIndex);
    routeFormationOwners.put(routeIndex, formationOwner);
    EntityManager.getInstance().add(formationOwner);
    return formationOwner;
  }

  public boolean isActive() {
   return true;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return "Route '" + getName() + "'";
  }
}
