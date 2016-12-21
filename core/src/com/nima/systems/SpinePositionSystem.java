package com.nima.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.nima.components.PositionComponent;
import com.nima.components.SpineComponent;
import com.nima.render.TiledMultiMapRenderer;

public class SpinePositionSystem extends IteratingSystem {
  private ComponentMapper<PositionComponent> positionMap = ComponentMapper.getFor(PositionComponent.class);
  private ComponentMapper<SpineComponent> spineMap = ComponentMapper.getFor(SpineComponent.class);

  private TiledMultiMapRenderer renderer;

  public SpinePositionSystem(TiledMultiMapRenderer renderer) {
    super(Family.all(SpineComponent.class).get());
    this.renderer = renderer;
  }

  public void processEntity(Entity entity, float deltaTime) {
    PositionComponent positionComponent = positionMap.get(entity);
    SpineComponent spineComponent = spineMap.get(entity);

    if(spineComponent != null) {
      spineComponent.setPosition(positionComponent.x, positionComponent.y);
    }
  }
}