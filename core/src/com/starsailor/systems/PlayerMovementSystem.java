package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.starsailor.actors.Player;
import com.starsailor.components.MovementComponent;
import com.starsailor.components.SpineComponent;

public class PlayerMovementSystem extends AbstractIteratingSystem {
  public PlayerMovementSystem() {
    super(Family.all(SpineComponent.class).get());
  }

  //TODO system required?
  public void process(Entity entity, float deltaTime) {
    if(entity instanceof Player) {
      MovementComponent movementComponent = entity.getComponent(MovementComponent.class);
      movementComponent.move();
    }

  }
}