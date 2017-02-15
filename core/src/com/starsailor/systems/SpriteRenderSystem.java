package com.starsailor.systems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.starsailor.components.GameEntityComponent;
import com.starsailor.components.SpriteComponent;

import java.util.Collection;

public class SpriteRenderSystem extends RenderingSystem {

  public SpriteRenderSystem(Batch batch) {
    super(batch, Family.all(GameEntityComponent.class).get());
  }

  @Override
  public void process(Entity entity, float deltaTime) {
    ImmutableArray<Component> components = entity.getComponents();
    for(Component component : components) {
      if(component instanceof SpriteComponent) {
        SpriteComponent spriteComponent = (SpriteComponent) component;
        //update the position of all sprites
        spriteComponent.updatePosition(entity);

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
}