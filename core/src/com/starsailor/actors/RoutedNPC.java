package com.starsailor.actors;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.FormationComponent;
import com.starsailor.data.ShipProfile;

/**
 * Patrolling a route
 */
public class RoutedNPC extends NPC {
  private Route route;
  private FormationComponent formationComponent;

  /**
   * Constructor used for ships that have been spawned from a route point
   * @param shipProfile
   * @param route
   */
  public RoutedNPC(ShipProfile shipProfile, Route route, State<NPC> defaultState, Vector2 position) {
    super(shipProfile, defaultState, position);
    this.route = route;
    this.route.npc = this;
  }

  @Override
  public void createComponents(ShipProfile profile) {
    super.createComponents(profile);
    routingComponent = ComponentFactory.addRoutingComponent(this, route);
    formationComponent = ComponentFactory.addFormationComponent(this, steerableComponent, 150f);
  }

  public void addGuard(GuardingNPC guardingNPC) {
    formationComponent.addMember(guardingNPC);
  }
}
