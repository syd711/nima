package com.starsailor.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.starsailor.components.RotationComponent;

public class RotationSystem extends AbstractIteratingSystem {
  private ComponentMapper<RotationComponent> rotationsMap = ComponentMapper.getFor(RotationComponent.class);

  public RotationSystem() {
    super(Family.all(RotationComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    RotationComponent rotationComponent = rotationsMap.get(entity);
    rotationComponent.updateRotation();
  }
}