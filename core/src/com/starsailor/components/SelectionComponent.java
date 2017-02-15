package com.starsailor.components;

import com.badlogic.ashley.core.Entity;
import com.starsailor.actors.Ship;
import com.starsailor.managers.Textures;

import static com.starsailor.managers.Textures.SELECTION;

/**
 * Stores the selection state
 */
public class SelectionComponent extends SpriteComponent {

  private boolean selected = false;

  @Override
  public void reset() {
    super.reset();
    this.selected = false;
  }

  public void setSelected(boolean b) {
    this.selected = b;
    if(selected) {
      addSprite(SELECTION);
    }
    else {
      removeSprite(SELECTION);
    }
  }

  public boolean isSelected() {
    return selected;
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
