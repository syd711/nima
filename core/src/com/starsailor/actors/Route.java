package com.starsailor.actors;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.states.route.RouteStates;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.RouteComponent;
import com.starsailor.components.StatefulComponent;
import com.starsailor.data.ShipProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a route
 */
public class Route extends GameEntity implements EntityListener {
  public RouteComponent routeComponent;
  public StatefulComponent statefulComponent;

  public NPC npc;
  public List<GuardingNPC> guardingNPCs = new ArrayList<>();
  public List<Guard> guards = new ArrayList<>();

  private String name;

  //the profile is needed here to respawn ships
  public ShipProfile shipProfile;
  public Behaviours behaviour = Behaviours.PEACEFUL;

  public Route(String name) {
    this.name = name;
    routeComponent = ComponentFactory.addRouteComponent(this);
    statefulComponent = ComponentFactory.addStatefulComponent(this);
    statefulComponent.stateMachine = new DefaultStateMachine<>(this, RouteStates.IDLE);
  }

  public boolean isActive() {
    return npc != null;
  }

  @Override
  public void entityAdded(Entity entity) {

  }

  @Override
  public void entityRemoved(Entity entity) {
    if(entity.equals(npc)) {
      npc = null;
    }
  }

  public void addGuard(ShipProfile ship, Vector2 centeredPosition, Behaviours behaviour) {
    guards.add(new Guard(ship, centeredPosition, behaviour));
  }

  public String getName() {
    return name;
  }

  public class Guard {
    public ShipProfile ship;
    public Vector2 centeredPosition;
    public Behaviours behaviour;

    public Guard(ShipProfile ship, Vector2 centeredPosition, Behaviours behaviour) {
      this.ship = ship;
      this.centeredPosition = centeredPosition;
      this.behaviour = behaviour;
    }
  }
}
