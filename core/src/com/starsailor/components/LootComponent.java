package com.starsailor.components;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

/**
 * Stores the selection state
 */
public class LootComponent extends SpriteComponent {
  private final static String SPRITE = "loot";

  @Override
  public void reset() {
    super.reset();
  }

  @Override
  protected void activate() {
    addSprite(SPRITE);
  }

  @Override
  protected void deactivate() {
    removeSprite(SPRITE);
  }

  @Override
  public void updatePosition(Entity entity) {

  }

  //only called 1x, not by a system
  public void setPosition(Vector2 position) {
    SpriteItem sprite = getSprite(SPRITE);
    sprite.setPosition(position, true);
  }
}
