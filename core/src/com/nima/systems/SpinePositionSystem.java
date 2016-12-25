package com.nima.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.nima.components.PositionComponent;
import com.nima.components.SpineComponent;

public class SpinePositionSystem extends AbstractIteratingSystem {
  private ComponentMapper<PositionComponent> positionMap = ComponentMapper.getFor(PositionComponent.class);
  private ComponentMapper<SpineComponent> spineMap = ComponentMapper.getFor(SpineComponent.class);

  public SpinePositionSystem() {
    super(Family.all(SpineComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    PositionComponent positionComponent = positionMap.get(entity);
    SpineComponent spineComponent = spineMap.get(entity);

    if(spineComponent != null) {
      spineComponent.setPosition(positionComponent.x, positionComponent.y);
    }
  }
}