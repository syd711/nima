package com.nima.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.nima.components.ScalingComponent;

public class ScalingSystem extends AbstractIteratingSystem {
  private ComponentMapper<ScalingComponent> skalingsMap = ComponentMapper.getFor(ScalingComponent.class);

  public ScalingSystem() {
    super(Family.all(ScalingComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    ScalingComponent scalingComponent = skalingsMap.get(entity);
    scalingComponent.updateValue();
  }
}