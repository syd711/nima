package com.starsailor.actors;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.starsailor.actors.states.RouteStates;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.RouteComponent;
import com.starsailor.components.StatefulComponent;

/**
 * Represents a route
 */
public class Route extends GameEntity {
  public RouteComponent routeComponent;
  public StatefulComponent statefulComponent;

  public Route(String name) {
    routeComponent = ComponentFactory.addRouteComponent(this, name);
    statefulComponent = ComponentFactory.addStatefulComponent(this);
    statefulComponent.stateMachine = new DefaultStateMachine<>(this, RouteStates.IDLE);
  }
}
