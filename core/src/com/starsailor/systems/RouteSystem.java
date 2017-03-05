package com.starsailor.systems;

import com.badlogic.ashley.core.Family;
import com.starsailor.actors.GameEntity;
import com.starsailor.actors.route.Route;
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
   
  }
}