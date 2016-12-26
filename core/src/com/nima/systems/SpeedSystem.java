package com.nima.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.nima.components.SpeedComponent;

public class SpeedSystem extends AbstractIteratingSystem {
  private ComponentMapper<SpeedComponent> speedsMap = ComponentMapper.getFor(SpeedComponent.class);

  public SpeedSystem() {
    super(Family.all(SpeedComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    SpeedComponent speed = speedsMap.get(entity);
    if(!speed.isAtTargetSpeed()) {
      speed.updateSpeed();
    }
  }
}