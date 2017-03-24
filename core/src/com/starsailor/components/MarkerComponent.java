package com.starsailor.components;

import com.badlogic.ashley.core.Entity;
import com.starsailor.actors.ClickTarget;

/**
 * Stores the selection state
 */
public class MarkerComponent extends SpriteComponent {
  private final static String MARKER = "marker";

  @Override
  public void reset() {
    super.reset();
  }

  @Override
  protected void activate() {
    addSprite(MARKER);
  }

  @Override
  protected void deactivate() {
    removeSprite(MARKER);
  }

  @Override
  public void updatePosition(Entity entity) {

  }

  //only called 1x, not by a system
  public void setPosition(Entity entity) {
    ClickTarget clickTarget = (ClickTarget) entity;
    SpriteItem sprite = getSprite(MARKER);
    sprite.setPosition(clickTarget.getCenter(), true);
  }
}
