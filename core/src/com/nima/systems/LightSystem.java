package com.nima.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.nima.components.LightComponent;
import com.nima.components.PositionComponent;

public class LightSystem extends AbstractIteratingSystem {
  private ComponentMapper<LightComponent> lightsMap = ComponentMapper.getFor(LightComponent.class);

  public LightSystem() {
    super(Family.all(LightComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    LightComponent light = lightsMap.get(entity);
    light.updateValue();
    if(light.isMoveable()) {
      PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
      light.setPosition(positionComponent.x, positionComponent.y);
    }
  }
}