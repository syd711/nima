package com.nima.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.nima.managers.GameStateManager;

/**
 * Common iterating system to be used to support game state
 */
abstract public class AbstractIteratingSystem extends IteratingSystem {

  public AbstractIteratingSystem(Family family) {
    super(family);
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    if(!GameStateManager.getInstance().isPaused()) {
      process(entity, deltaTime);
    }
  }

  abstract public void process(Entity entity, float deltaTime);
}
