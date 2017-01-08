package com.nima.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.nima.actors.Player;
import com.nima.components.MovementComponent;
import com.nima.components.SpineComponent;

public class PlayerMovementSystem extends AbstractIteratingSystem {
  public PlayerMovementSystem() {
    super(Family.all(SpineComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    if(entity instanceof Player) {
      MovementComponent movementComponent = entity.getComponent(MovementComponent.class);
      movementComponent.move();
    }

  }
}