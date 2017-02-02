package com.starsailor.actors;

import com.badlogic.gdx.utils.Array;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.FormationComponent;
import com.starsailor.components.SteerableComponent;
import com.starsailor.data.ShipProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * Patrolling a route
 */
public class RoutedNPC extends NPC {

  private Route route;
  private List<GuardingNPC> guards = new ArrayList<>();
  private FormationComponent formationComponent;

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
    formationComponent = ComponentFactory.addFormationComponent(this, steerableComponent, 150f);
  }

  public void addGuard(GuardingNPC guardingNPC) {
    guards.add(guardingNPC);
    formationComponent.addMember(guardingNPC);
  }

  public Array<SteerableComponent> getGuardSteerings() {
    List<SteerableComponent> steerableComponents = new ArrayList<>();
    for(GuardingNPC guard : guards) {
      steerableComponents.add(guard.steerableComponent);
    }
    return new Array<>(steerableComponents.toArray(new SteerableComponent[steerableComponents.size()]));
  }
}
