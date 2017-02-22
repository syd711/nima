package com.starsailor.components;

import com.badlogic.ashley.core.Entity;
import com.starsailor.actors.Ship;

/**
 * Stores the selection state
 */
public class SelectionComponent extends SpriteComponent {
  private final static String SELECTION = "selection";

  @Override
  public void reset() {
    super.reset();
  }

  @Override
  protected void activate() {
    addSprite(SELECTION);
  }

  @Override
  protected void deactivate() {
    removeSprite(SELECTION);
  }

  @Override
  public void updatePosition(Entity entity) {
    Ship ship = (Ship) entity;
    SpriteItem sprite = getSprite(SELECTION);
    if(sprite != null) {
      sprite.setPosition(ship.getCenter(), true);
    }
  }
}
