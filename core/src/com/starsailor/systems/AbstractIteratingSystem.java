package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

/**
 * Common iterating system to be used to support game state
 */
abstract public class AbstractIteratingSystem extends IteratingSystem {

  public AbstractIteratingSystem(Family family) {
    super(family);
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    process(entity, deltaTime);
  }

  abstract public void process(Entity entity, float deltaTime);
}
