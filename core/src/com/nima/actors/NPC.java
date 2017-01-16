package com.nima.actors;

import com.badlogic.gdx.ai.fsm.State;
import com.nima.components.ComponentFactory;
import com.nima.components.RoutingComponent;
import com.nima.data.ShipProfile;
import com.nima.render.converters.MapConstants;

/**
 * Common superclass for all NPC.
 * We assume that they are instances of Spine.
 */
public class NPC extends Ship {
  public RoutingComponent routingComponent;

  private Route route;

  public NPC(ShipProfile shipProfile, Route route, State state) {
    super(shipProfile, state);
    this.route = route;
    routingComponent = ComponentFactory.addRoutingComponent(this, route);
    ComponentFactory.addNPCCollisionComponent(this);
  }

  public boolean isAggressive() {
    return getBehaviour() != null && getBehaviour().equalsIgnoreCase(MapConstants.BEHAVIOUR_AGGRESSIVE);
  }

  public String getBehaviour() {
    return route.routeComponent.behaviour;
  }
}
