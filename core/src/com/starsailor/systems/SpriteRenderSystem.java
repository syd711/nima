package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.starsailor.components.SpriteComponent;

import java.util.Collection;

public class SpriteRenderSystem extends SortedIteratingSystem {
  private Batch batch;

  public SpriteRenderSystem(Batch batch) {
    super(Family.all(SpriteComponent.class).get(), new ZComparator());
    this.batch = batch;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
    batch.setShader(null);

    if(spriteComponent != null) {
      Collection<SpriteComponent.SpriteItem> spriteItems = spriteComponent.getSpriteItems();
      for(SpriteComponent.SpriteItem spriteItem : spriteItems) {
        if(spriteItem.isPositioned()) {
          spriteItem.sprite.draw(batch);
        }
      }
    }
  }
}