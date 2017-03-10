package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.starsailor.managers.ResourceManager;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;


public class SpriteComponent implements Component, Poolable {

  private Map<String, SpriteItem> sprites = new LinkedHashMap<>();

  private boolean active = false;

  @Override
  public void reset() {
    sprites.clear();
  }

  public void addSprite(String sprite) {
    addSprite(sprite, -1);
  }

  public void addSprite(String sprite, float angle) {
    SpriteItem spriteItem = new SpriteItem(sprite);
    if(sprites.containsKey(sprite)) {
      throw new UnsupportedOperationException("Sprite already exists for this component");
    }

    if(angle > 0) {
      spriteItem.getSprite().setRotation(angle);
    }
    sprites.put(sprite, spriteItem);
  }

  public SpriteItem getSprite(String s) {
    return sprites.get(s);
  }

  public Collection<SpriteItem> getSpriteItems() {
    return sprites.values();
  }

  public void removeSprite(String textures) {
    sprites.remove(textures);
  }


  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    if(active != this.active) {
      if(active) {
        activate();
      }
      else {
        deactivate();
      }
    }
    this.active = active;
  }

  /**
   * To be implemented for subclasses, ensured that it is only called when not activated yet
   */
  protected void activate() {
    //add or remove sprites
  }

  /**
   * To be implemented for subclasses, ensured that it is only called when activated
   */
  protected void deactivate() {
    //add or remove sprites
  }

  /**
   * Empty method that may be overwritten by a subclass to update
   * the position of sprites before rendering
   * @param entity the owner of this sprite component
   */
  public void updatePosition(Entity entity) {
    //update the position of the sprite map
  }

  /**
   * Helper to support multiple sprites for one component.
   */
  public class SpriteItem {
    public Sprite sprite;
    private String name;

    public SpriteItem(String spriteName) {
      name = spriteName;
      sprite = new Sprite(ResourceManager.getInstance().getTextureAsset(spriteName));
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
