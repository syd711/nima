package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

        if(spriteItem.isTexture()) {
          Sprite sprite = spriteItem.getSprite();
          Texture texture = spriteItem.getTexture();
          TextureRegion region = new TextureRegion(texture, 400, 12);
          batch.draw(region, sprite.getX(), sprite.getY(), sprite.getOriginX(), sprite.getOriginY(), sprite.getWidth(), sprite.getHeight(), 1,1, sprite.getRotation());
        }
        else {
          Sprite sprite = spriteItem.getSprite();
          sprite.draw(batch);
        }

      }
    }
  }
}