package com.nima.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.nima.components.CollisionComponent;
import com.nima.components.MovementComponent;
import com.nima.components.SpineComponent;

public class SpineMovementSystem extends AbstractIteratingSystem {
  public SpineMovementSystem() {
    super(Family.all(SpineComponent.class).get());
  }

  public void process(Entity spineEntity, float deltaTime) {
    MovementComponent movementComponent = spineEntity.getComponent(MovementComponent.class);
    CollisionComponent collisionComponent = spineEntity.getComponent(CollisionComponent.class);
    movementComponent.move();
  }
}