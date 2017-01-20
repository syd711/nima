package com.starsailor.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.starsailor.actors.Spine;
import com.starsailor.components.ScalingComponent;

public class ScalingSystem extends AbstractIteratingSystem {
  private ComponentMapper<ScalingComponent> skalingsMap = ComponentMapper.getFor(ScalingComponent.class);

  public ScalingSystem() {
    super(Family.all(ScalingComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    ScalingComponent scalingComponent = skalingsMap.get(entity);
    scalingComponent.updateValue();

    if(entity instanceof Spine) {
      Spine spine = (Spine) entity;
      float scaleX = spine.skeleton.getRootBone().getScaleX();
      if(scaleX != scalingComponent.getCurrentValue()) {
        spine.skeleton.getRootBone().setScale(scalingComponent.getCurrentValue());
      }
    }
  }
}