package com.nima.actors;

import com.nima.actors.states.NPCState;
import com.nima.components.ComponentFactory;
import com.nima.components.RoutingComponent;
import com.nima.data.ShipProfile;

/**
 * Common superclass for all NPC.
 * We assume that they are instances of Spine.
 */
public class NPC extends Ship {
  public RoutingComponent routingComponent;

  public NPC(ShipProfile shipProfile, Route route, NPCState state) {
    super(shipProfile, state);
    routingComponent = ComponentFactory.addRoutingComponent(this, route);
  }
}
