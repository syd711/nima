package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.starsailor.managers.Sprites;
import com.starsailor.managers.TextureManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class SpriteComponent implements Component, Poolable {

  private Map<Sprites, SpriteItem> sprites = new HashMap<>();

  @Override
  public void reset() {
    sprites.clear();
  }

  public void addSprite(Sprites sprite, float angle) {
    SpriteItem spriteItem = new SpriteItem(sprite);
    if(sprites.containsKey(sprite)) {
      throw new UnsupportedOperationException("Sprite already exists for this component");
    }

    if(angle > 0) {
      spriteItem.sprite.setRotation(angle);
    }
    sprites.put(sprite, spriteItem);
  }

  public SpriteItem getSprite(Sprites s) {
    return sprites.get(s);
  }

  public Collection<SpriteItem> getSpriteItems() {
    return sprites.values();
  }

  /**
   * Helper to support multiple sprites for one component.
   */
  public class SpriteItem {
    public Sprite sprite;

    public SpriteItem(Sprites spriteEnum) {
      sprite = TextureManager.getInstance().createSprite(spriteEnum);
    }

    public void updateRotation(float angle) {
      sprite.setRotation(angle);
    }

    public void updatePosition(Vector2 pos, boolean centered) {
      if(centered) {
        sprite.setPosition(pos.x-sprite.getWidth()/2, pos.y-sprite.getHeight()/2);
      }
      else {
        sprite.setPosition(pos.x, pos.y);
      }
    }
  }
}
