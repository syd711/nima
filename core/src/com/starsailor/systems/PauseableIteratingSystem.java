package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.starsailor.Game;
import com.starsailor.GameState;
import com.starsailor.actors.GameEntity;

/**
 * Common iterating system to be used to support game state
 */
abstract public class PauseableIteratingSystem extends IteratingSystem {

  public PauseableIteratingSystem(Family family) {
    super(family);
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    if(!Game.gameState.getCurrentState().equals(GameState.PAUSED)) {
      process((GameEntity) entity, deltaTime);
    }
  }

  abstract public void process(GameEntity entity, float deltaTime);
}
