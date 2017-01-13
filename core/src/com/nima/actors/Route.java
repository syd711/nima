package com.nima.actors;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.nima.actors.states.RouteState;
import com.nima.components.ComponentFactory;
import com.nima.components.RouteComponent;
import com.nima.components.StatefulComponent;

/**
 * Represents a route
 */
public class Route extends Entity {
  public RouteComponent routeComponent;
  public StatefulComponent statefulComponent;

  public Route(String name) {
    routeComponent = ComponentFactory.addRouteComponent(this, name);
    statefulComponent = ComponentFactory.addStatefulComponent(this);
    statefulComponent.stateMachine = new DefaultStateMachine<>(this, RouteState.IDLE);
  }
}
