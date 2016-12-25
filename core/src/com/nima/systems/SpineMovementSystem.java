package com.nima.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.nima.components.MovementComponent;
import com.nima.components.SpineComponent;

public class SpineMovementSystem extends AbstractIteratingSystem {
  public SpineMovementSystem() {
    super(Family.all(SpineComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    MovementComponent movementComponent = entity.getComponent(MovementComponent.class);
    movementComponent.move();
  }
}