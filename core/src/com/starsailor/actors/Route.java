package com.starsailor.actors;

import com.badlogic.gdx.ai.fsm.StackStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.states.route.RouteStates;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.FractionComponent;
import com.starsailor.components.RouteComponent;
import com.starsailor.components.StatefulComponent;
import com.starsailor.data.ShipProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a route
 */
public class Route extends GameEntity {
  public FractionComponent fractionComponent;
  public RouteComponent routeComponent;
  public StatefulComponent statefulComponent;

  public List<RouteMember> members = new ArrayList<>();

  private String name;

  //the profile is needed here to respawn ships
  public ShipProfile shipProfile;

  public Route(String name) {
    this.name = name;
    routeComponent = ComponentFactory.addRouteComponent(this);
    statefulComponent = ComponentFactory.addStatefulComponent(this);
    statefulComponent.stateMachine = new StackStateMachine(this, RouteStates.IDLE);
    fractionComponent = ComponentFactory.createFractionComponent(this, null);
  }

  public boolean isActive() {
   return true;
  }

  public void addMember(ShipProfile ship, Vector2 centeredPosition, State state) {
    members.add(new RouteMember(ship, centeredPosition, state));
  }

  public String getName() {
    return name;
  }

  public class RouteMember {
    public ShipProfile shipProfile;
    public Vector2 position;
    public State state;

    public RouteMember(ShipProfile ship, Vector2 centeredPosition, State state) {
      this.shipProfile = ship;
      this.position = centeredPosition;
      this.state = state;
    }
  }
}
