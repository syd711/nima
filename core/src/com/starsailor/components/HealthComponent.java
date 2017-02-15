package com.starsailor.components;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.Ship;

import static com.starsailor.managers.Textures.HEALTHBG;
import static com.starsailor.managers.Textures.HEALTHFG;

/**
 * Stores the health state
 */
public class HealthComponent extends SpriteComponent {
  public float maxHealth;
  public float health;

  @Override
  public void reset() {
    super.reset();

    this.health = 100; //not real values
    this.maxHealth = 100; //not real values
  }

  public float getPercent() {
    return health * 100 / maxHealth;
  }

  public void setActive(boolean active) {
    if(active) {
      addSprite(HEALTHBG);
      addSprite(HEALTHFG);
    }
    else {
      removeSprite(HEALTHBG);
      removeSprite(HEALTHFG);
    }
  }

  @Override
  public void updatePosition(Entity entity) {
    Ship ship = (Ship) entity;
    Vector2 pos = new Vector2(ship.getCenter());
    pos.y += 120;
    pos.x -= ship.getWidth() / 2 - 10;

    SpriteComponent.SpriteItem background = getSprite(HEALTHBG);
    if(background != null) {
      background.setPosition(pos, false);
      SpriteComponent.SpriteItem foreGround = getSprite(HEALTHFG);
      foreGround.setPosition(pos, false);

      float healthPercentage = health * 100 / maxHealth;
      float healthWidth = foreGround.getSprite().getTexture().getWidth() * healthPercentage / 100;
      foreGround.setWidth(healthWidth);
    }
  }
}
