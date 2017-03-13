package com.starsailor.systems;

import com.badlogic.ashley.core.Family;
import com.starsailor.actors.GameEntity;
import com.starsailor.components.BodyComponent;
import com.starsailor.components.SpineShieldComponent;
import com.starsailor.components.SpineShipComponent;

public class SpineUpdateSystem extends PauseableIteratingSystem {

  public SpineUpdateSystem() {
    super(Family.all(BodyComponent.class).get()); //TODO add marker
  }

  public void process(GameEntity entity, float deltaTime) {
    SpineShipComponent spineShipComponent = entity.getComponent(SpineShipComponent.class);
    if(spineShipComponent != null) {
      spineShipComponent.getAnimationState().update(deltaTime); // Update the animation time.
    }


    SpineShieldComponent spineShieldComponent = entity.getComponent(SpineShieldComponent.class);
    if(spineShieldComponent != null) {
      spineShieldComponent.getAnimationState().update(deltaTime); // Update the animation time.
    }
  }
}