package com.starsailor.components;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.starsailor.actors.Ship;

import static com.starsailor.managers.Textures.SHIELDBG;
import static com.starsailor.managers.Textures.SHIELDFG;

/**
 *
 */
public class ShieldComponent extends SpriteComponent {
  public float maxHealth;
  public float health;
  public float rechargeTimeMillis;
  public float damageAbsorptionFactor;

  private boolean active = false;

  public Body body;

  @Override
  public void reset() {
    super.reset();
    this.maxHealth = 0;
    this.health = 0;
  }

  public boolean isActive() {
    return active;
  }

  public float applyDamage(float damage) {
    this.health = health - (damage * damageAbsorptionFactor);
    if(this.health <= 0) {
      setActive(false);
      //normalize remaining damage value again
      return Math.abs(health/damageAbsorptionFactor);
    }
    return 0;
  }

  public void setActive(boolean active) {
    this.active = active;

    //TODO custom sprite for shield
    if(active) {
      addSprite(SHIELDBG);
      addSprite(SHIELDFG);
    }
    else {
      removeSprite(SHIELDBG);
      removeSprite(SHIELDFG);
    }
  }

  @Override
  public void updatePosition(Entity entity) {
    Ship ship = (Ship) entity;
    Vector2 pos = new Vector2(ship.getCenter());
    pos.y += 145;
    pos.x -= ship.getWidth() / 2 - 10;

    SpriteComponent.SpriteItem background = getSprite(SHIELDBG);
    if(background  != null) {
      background.setPosition(pos, false);
      SpriteComponent.SpriteItem foreGround = getSprite(SHIELDFG);
      foreGround.setPosition(pos, false);

      float healthPercentage = health * 100 / maxHealth;
      float healthWidth = foreGround.getSprite().getTexture().getWidth() * healthPercentage / 100;
      foreGround.setWidth(healthWidth);
    }
  }

  public boolean isRemaining() {
    return health > 0;
  }
}
