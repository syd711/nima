package com.nima.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.nima.components.SpineComponent;

public class SpineRenderSystem extends AbstractIteratingSystem {
  private ComponentMapper<SpineComponent> spineMap = ComponentMapper.getFor(SpineComponent.class);
  private BatchTiledMapRenderer renderer;

  public SpineRenderSystem(BatchTiledMapRenderer renderer) {
    super(Family.all(SpineComponent.class).get());
    this.renderer = renderer;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    process(entity, deltaTime);
  }

  public void process(Entity entity, float deltaTime) {
    SpineComponent spineComponent = spineMap.get(entity);

    if(spineComponent != null) {
      spineComponent.render(renderer);
    }
  }
}