package com.starsailor.systems;

import com.badlogic.ashley.core.Family;
import com.starsailor.actors.GameEntity;
import com.starsailor.components.SpineComponent;

public class SpineUpdateSystem extends PauseableIteratingSystem {

  public SpineUpdateSystem() {
    super(Family.all(SpineComponent.class).get());
  }

  public void process(GameEntity entity, float deltaTime) {
    SpineComponent spineComponent = entity.getComponent(SpineComponent.class);
    spineComponent.getAnimationState().update(deltaTime); // Update the animation time.
  }
}