package com.starsailor.actors;

import com.starsailor.components.ComponentFactory;
import com.starsailor.data.ShipProfile;

/**
 * Patrolling a route
 */
public class RoutedNPC extends NPC {

  private Route route;

  /**
   * Constructor used for ships that have been spawned from a route point
   * @param shipProfile
   * @param route
   */
  public RoutedNPC(ShipProfile shipProfile, Route route, Behaviours behaviour) {
    super(shipProfile, behaviour);
    this.route = route;
    this.route.npc = this;
    this.behaviour = behaviour;
  }

  @Override
  public void createComponents(ShipProfile profile) {
    super.createComponents(profile);
    routingComponent = ComponentFactory.addRoutingComponent(this, route);
  }
}
