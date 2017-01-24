package com.starsailor.actors;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.starsailor.actors.states.RouteStates;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.RouteComponent;
import com.starsailor.components.StatefulComponent;

/**
 * Represents a route
 */
public class Route extends GameEntity implements EntityListener {
  public RouteComponent routeComponent;
  public StatefulComponent statefulComponent;

  public Route(String name) {
    routeComponent = ComponentFactory.addRouteComponent(this, name);
    statefulComponent = ComponentFactory.addStatefulComponent(this);
    statefulComponent.stateMachine = new DefaultStateMachine<>(this, RouteStates.IDLE);
  }

  public boolean isActive() {
    return routeComponent.npc != null;
  }

  @Override
  public void entityAdded(Entity entity) {

  }

  @Override
  public void entityRemoved(Entity entity) {
    if(entity.equals(routeComponent.npc)) {
      routeComponent.npc = null;
    }
  }
}
