package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.starsailor.managers.Sprites;
import com.starsailor.managers.TextureManager;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;


public class SpriteComponent implements Component, Poolable {

  private Map<Sprites, SpriteItem> sprites = new LinkedHashMap<>();

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
    private String name;
    private float width;

    public SpriteItem(Sprites spriteEnum) {
      name = spriteEnum.name();
      sprite = TextureManager.getInstance().createSprite(spriteEnum);
    }

    public void setRotation(float angle) {
      sprite.setRotation(angle);
    }

    public void setPosition(Vector2 pos, boolean centered) {
      if(centered) {
        sprite.setPosition(pos.x-sprite.getWidth()/2, pos.y-sprite.getHeight()/2);
      }
      else {
        sprite.setPosition(pos.x, pos.y);
      }
    }

    public void setScale(float scale) {
      sprite.setScale(scale);
    }

    public void setScale(float scaleX, float scaleY) {
      sprite.setScale(scaleX, scaleY);
    }

    public boolean isPositioned() {
      return sprite.getX() != 0;
    }

    public void setSize(float width, float height) {
      sprite.setSize(width, height);
    }

    public void setWidth(float width) {
      sprite.setSize(width, sprite.getHeight());
    }

    @Override
    public String toString() {
      return "Sprite '" + name + "'";
    }
  }
}
