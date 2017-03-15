package com.starsailor.systems;

import com.badlogic.ashley.core.Family;
import com.starsailor.actors.GameEntity;
import com.starsailor.components.ShipBodyComponent;
import com.starsailor.components.SpineComponent;

import java.util.List;

public class SpineUpdateSystem extends PauseableIteratingSystem {

  public SpineUpdateSystem() {
    super(Family.all(ShipBodyComponent.class).get()); //TODO add marker
  }

  public void process(GameEntity entity, float deltaTime) {
    List<SpineComponent> spineComponents = entity.getComponents(SpineComponent.class);
    for(SpineComponent spineComponent : spineComponents) {
      spineComponent.getAnimationState().update(deltaTime); // Update the animation time.
    }
  }
}