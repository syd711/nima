package com.starsailor.actors;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.ai.fsm.StackStateMachine;
import com.badlogic.gdx.ai.fsm.State;
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
  public List<NPC> routeMembers = new ArrayList<>();
  public List<RouteMember> members = new ArrayList<>();

  private String name;

  //the profile is needed here to respawn ships
  public ShipProfile shipProfile;

  public Route(String name) {
    this.name = name;
    routeComponent = ComponentFactory.addRouteComponent(this);
    statefulComponent = ComponentFactory.addStatefulComponent(this);
    statefulComponent.stateMachine = new StackStateMachine(this, RouteStates.IDLE);
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

  public void addMember(ShipProfile ship, Vector2 centeredPosition, State state) {
    members.add(new RouteMember(ship, centeredPosition, state));
  }

  public String getName() {
    return name;
  }

  public class RouteMember {
    public ShipProfile ship;
    public Vector2 centeredPosition;
    public State state;

    public RouteMember(ShipProfile ship, Vector2 centeredPosition, State state) {
      this.ship = ship;
      this.centeredPosition = centeredPosition;
      this.state = state;
    }
  }
}
