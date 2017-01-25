package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.starsailor.components.SpriteComponent;

import java.util.Collection;

//TODO create sprite system since not all systems render, so call begin and end!
public class SpriteRenderSystem extends SortedIteratingSystem {
  private SpriteBatch batch;

  public SpriteRenderSystem(Batch batch) {
    super(Family.all(SpriteComponent.class).get(), new ZComparator());
    this.batch = (SpriteBatch) batch;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);

    if(spriteComponent != null) {
      Collection<SpriteComponent.SpriteItem> spriteItems = spriteComponent.getSpriteItems();
      for(SpriteComponent.SpriteItem spriteItem : spriteItems) {
        if(spriteItem.isPositioned()) {
          Sprite sprite = spriteItem.getSprite();
          sprite.draw(batch);
        }
      }
    }
  }
}