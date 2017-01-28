package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.starsailor.managers.TextureManager;
import com.starsailor.managers.Textures;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;


public class SpriteComponent implements Component, Poolable {

  private Map<Textures, SpriteItem> sprites = new LinkedHashMap<>();

  private boolean enabled = true;

  @Override
  public void reset() {
    enabled = true;
    sprites.clear();
  }

  public void addSprite(Textures sprite) {
    addSprite(sprite, -1);
  }

  public void addSprite(Textures sprite, float angle) {
    SpriteItem spriteItem = new SpriteItem(sprite);
    if(sprites.containsKey(sprite)) {
      throw new UnsupportedOperationException("Sprite already exists for this component");
    }

    if(angle > 0) {
      spriteItem.getSprite().setRotation(angle);
    }
    sprites.put(sprite, spriteItem);
  }

  public SpriteItem getSprite(Textures s) {
    return sprites.get(s);
  }

  public Collection<SpriteItem> getSpriteItems() {
    return sprites.values();
  }

  public void removeSprite(Textures textures) {
    sprites.remove(textures);
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * Helper to support multiple sprites for one component.
   */
  public class SpriteItem {
    public Sprite sprite;
    private String name;

    public SpriteItem(Textures spriteEnum) {
      name = spriteEnum.name();
      sprite = new Sprite(TextureManager.getInstance().getTexture(spriteEnum));
    }

    public Sprite getSprite() {
      return sprite;
    }

    public void setRotation(float angle) {
      sprite.setRotation(angle);
    }

    public void setPosition(Vector2 pos, boolean centered) {
      if(centered) {
        getSprite().setPosition(pos.x-sprite.getWidth()/2, pos.y-sprite.getHeight()/2);
      }
      else {
        getSprite().setPosition(pos.x, pos.y);
      }
    }

    public void setScale(float scale) {
      sprite.setScale(scale);
    }

    public boolean isPositioned() {
      return getSprite().getX() != 0;
    }

    public void setWidth(float width) {
      getSprite().setSize(width, sprite.getHeight());
    }

    @Override
    public String toString() {
      return "Sprite '" + name + "'";
    }
  }
}
