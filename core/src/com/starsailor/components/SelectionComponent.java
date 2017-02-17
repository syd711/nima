package com.starsailor.components;

import com.badlogic.ashley.core.Entity;
import com.starsailor.actors.Ship;
import com.starsailor.managers.Textures;

import static com.starsailor.managers.Textures.SELECTION;

/**
 * Stores the selection state
 */
public class SelectionComponent extends SpriteComponent {

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
    SpriteItem sprite = getSprite(Textures.SELECTION);
    if(sprite != null) {
      sprite.setPosition(ship.getCenter(), true);
    }
  }
}
