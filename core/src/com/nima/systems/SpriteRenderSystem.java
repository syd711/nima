package com.nima.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.nima.components.SpriteComponent;

public class SpriteRenderSystem extends SortedIteratingSystem {
  private Batch batch;

  private ComponentMapper<SpriteComponent> spriteMap = ComponentMapper.getFor(SpriteComponent.class);

  public SpriteRenderSystem(Batch batch) {
    super(Family.all(SpriteComponent.class).get(), new ZComparator());
    this.batch = batch;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    Sprite sprite = spriteMap.get(entity).sprite;
    batch.setShader(null);
    sprite.draw(batch);
  }
}