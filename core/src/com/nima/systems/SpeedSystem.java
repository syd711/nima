package com.nima.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.nima.components.SpeedComponent;

public class SpeedSystem extends AbstractIteratingSystem {
  private ComponentMapper<SpeedComponent> speedMap = ComponentMapper.getFor(SpeedComponent.class);

  public SpeedSystem() {
    super(Family.all(SpeedComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    SpeedComponent speedComponent = speedMap.get(entity);
    speedComponent.updateValue();
  }
}