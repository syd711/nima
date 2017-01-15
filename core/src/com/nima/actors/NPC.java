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

  public NPC(ShipProfile shipProfile, Route route, State state) {
    super(shipProfile, state);
    routingComponent = ComponentFactory.addRoutingComponent(this, route);
  }
}
