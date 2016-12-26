package com.nima.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.nima.components.CollisionComponent;
import com.nima.components.MovementComponent;
import com.nima.components.SpeedComponent;
import com.nima.components.SpineComponent;
import com.nima.managers.GameStateManager;

public class SpineMovementSystem extends AbstractIteratingSystem {
  public SpineMovementSystem() {
    super(Family.all(SpineComponent.class).get());
  }

  public void process(Entity spineEntity, float deltaTime) {
    MovementComponent movementComponent = spineEntity.getComponent(MovementComponent.class);

    //check if the entity has reached it's target
    if(movementComponent.getTarget() != null) {
      CollisionComponent movingObjectCollision = spineEntity.getComponent(CollisionComponent.class);
      SpeedComponent speedComponent = spineEntity.getComponent(SpeedComponent.class);
      CollisionComponent targetEntityCollision = movementComponent.getTarget().getComponent(CollisionComponent.class);
      if(targetEntityCollision != null) {
        if(movingObjectCollision.isColliding(targetEntityCollision) && speedComponent.getTargetSpeed() > 0) {
          movementComponent.stop();
          GameStateManager.getInstance().setInGameMenu(true);
          return;
        }
      }
    }


    movementComponent.move();
  }
}