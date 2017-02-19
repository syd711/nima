package com.starsailor.systems;

import com.badlogic.ashley.core.Family;
import com.starsailor.actors.GameEntity;
import com.starsailor.actors.Route;
import com.starsailor.actors.states.route.RouteStates;
import com.starsailor.components.RouteComponent;

/**
 * This system deals with the active routes.
 * Other systems ensure that the route entity is still in the engine,
 * so we only have to check the ship status on the route.
 */
public class RouteSystem extends PauseableIteratingSystem {
  public RouteSystem() {
    super(Family.all(RouteComponent.class).get());
  }

  public void process(GameEntity entity, float deltaTime) {
    Route route = (Route) entity;
    if(route.statefulComponent.stateMachine.getCurrentState().equals(RouteStates.IDLE)) {
      route.statefulComponent.stateMachine.changeState(RouteStates.SPAWN_SHIPS);
    }
    else if(!route.isActive()) {
      route.statefulComponent.stateMachine.changeState(RouteStates.INACTIVE);
    }
  }
}