package com.nima.actors;

import com.badlogic.gdx.ai.fsm.State;
import com.nima.components.ComponentFactory;
import com.nima.components.RoutingComponent;
import com.nima.data.ShipProfile;

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

  public float distanceToPlayer() {
    return positionComponent.getPosition().dst(Player.getInstance().positionComponent.getPosition());
  }

  public String getBehaviour() {
    return route.routeComponent.behaviour;
  }
}
