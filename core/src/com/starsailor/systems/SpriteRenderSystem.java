package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.Ship;
import com.starsailor.components.ShieldComponent;
import com.starsailor.components.SpriteComponent;
import com.starsailor.managers.Textures;

import java.util.Collection;

public class SpriteRenderSystem extends RenderingSystem {

  public SpriteRenderSystem(Batch batch) {
    super(batch, Family.all(SpriteComponent.class).get());
  }

  @Override
  public void process(Entity entity, float deltaTime) {
    if(entity instanceof Ship) {
      Ship ship = (Ship) entity;
      ShieldComponent shieldComponent = ship.shieldComponent;

      //update health sprites
      updateHealthSprites(ship, shieldComponent);
    }

    SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
    Collection<SpriteComponent.SpriteItem> spriteItems = spriteComponent.getSpriteItems();
    for(SpriteComponent.SpriteItem spriteItem : spriteItems) {
      if(spriteItem.isPositioned() && spriteComponent.isEnabled()) {
        Sprite sprite = spriteItem.getSprite();
        sprite.draw(batch);
      }
    }
  }


  private void updateHealthSprites(Ship ship, ShieldComponent shieldComponent) {
    updatePositionAndHealth(ship, Textures.HEALTHBG, Textures.HEALTHFG, 120, ship.healthComponent.health, ship.healthComponent.maxHealth);

    if(shieldComponent.isActive()) {
      //update shield sprite
      ShieldComponent shield = ship.shieldComponent;
      updatePositionAndHealth(ship, Textures.SHIELDBG, Textures.SHIELDFG, 145, shield.health, shield.maxHealth);
    }
  }

  /**
   * Updates the health and shield status bars
   * @param ship
   * @param textures1
   * @param textures2
   * @param y
   * @param health
   * @param maxHealth
   */
  private void updatePositionAndHealth(Ship ship, Textures textures1, Textures textures2, int y, float health, float maxHealth) {
    Vector2 pos = new Vector2(ship.getCenter());
    pos.y += y;
    pos.x -= ship.getWidth() / 2 - 10;

    SpriteComponent.SpriteItem background = ship.spriteComponent.getSprite(textures1);
    if(background  != null) {
      background.setPosition(pos, false);
      SpriteComponent.SpriteItem foreGround = ship.spriteComponent.getSprite(textures2);
      foreGround.setPosition(pos, false);

      float healthPercentage = health * 100 / maxHealth;
      float healthWidth = foreGround.getSprite().getTexture().getWidth() * healthPercentage / 100;
      foreGround.setWidth(healthWidth);
    }
  }
}