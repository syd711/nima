package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.starsailor.actors.Spine;
import com.starsailor.components.SpineComponent;

public class SpineUpdateSystem extends PauseableIteratingSystem {

  public SpineUpdateSystem() {
    super(Family.all(SpineComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    ((Spine)entity).state.update(deltaTime); // Update the animation time.
  }
}