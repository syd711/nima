package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.starsailor.components.SpriteComponent;

import java.util.Collection;

public class SpriteRenderSystem extends RenderingSystem {

  public SpriteRenderSystem(Batch batch) {
    super(batch, Family.all(SpriteComponent.class).get());
  }

  @Override
  public void process(Entity entity, float deltaTime) {
    SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
    Collection<SpriteComponent.SpriteItem> spriteItems = spriteComponent.getSpriteItems();
    for(SpriteComponent.SpriteItem spriteItem : spriteItems) {
      if(spriteItem.isPositioned() && spriteComponent.isEnabled()) {
        Sprite sprite = spriteItem.getSprite();
        sprite.draw(batch);
      }
    }
  }
}