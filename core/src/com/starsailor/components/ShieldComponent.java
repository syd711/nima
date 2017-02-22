package com.starsailor.components;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.starsailor.Game;
import com.starsailor.actors.Ship;

/**
 *
 */
public class ShieldComponent extends SpriteComponent {
  private final static String SHIELDBG = "shieldbg";
  private final static String SHIELDFG = "shieldfg";

  public float maxHealth;
  public float health;
  public float rechargeTimeMillis;
  public float damageAbsorptionFactor;

  public Body body;

  @Override
  public void reset() {
    super.reset();
    this.maxHealth = 0;
    this.health = 0;
    this.body = null;
  }

  public void destroy() {
    if(body != null) {
      Game.world.destroyBody(body);
      body = null;
    }
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

  @Override
  protected void activate() {
    addSprite(SHIELDBG);
    addSprite(SHIELDFG);
  }

  @Override
  protected void deactivate() {
    removeSprite(SHIELDBG);
    removeSprite(SHIELDFG);
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
