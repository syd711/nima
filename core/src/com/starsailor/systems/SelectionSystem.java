package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.NPC;
import com.starsailor.components.SelectionComponent;
import com.starsailor.components.ShieldComponent;
import com.starsailor.components.SpriteComponent;
import com.starsailor.managers.Textures;

/**
 * Handles the selection and the changed selection rendering
 */
public class SelectionSystem extends AbstractIteratingSystem {

  public SelectionSystem() {
    super(Family.all(SelectionComponent.class).get());
  }

  @Override
  public void process(Entity entity, float deltaTime) {
    if(!(entity instanceof NPC)) {
      return;
    }

    NPC npc = (NPC) entity;
    SpriteComponent spriteComponent = npc.spriteComponent;
    ShieldComponent shieldComponent = npc.shieldComponent;

    //update selection sprite
    if(npc.selectionComponent.selected) {
      //update selection sprite
      Vector2 center = npc.getCenter();
      spriteComponent.getSprite(Textures.SELECTION).setPosition(center, true);

      //update health sprites
      updateHealthSprites(npc, shieldComponent);
    }
  }

  private void updateHealthSprites(NPC npc, ShieldComponent shieldComponent) {
    updatePositionAndHealth(npc, Textures.HEALTHBG, Textures.HEALTHFG, 120, npc.health, npc.maxHealth);

    if(shieldComponent.isActive()) {
      //update shield sprite
      ShieldComponent shield = npc.shieldComponent;
      updatePositionAndHealth(npc, Textures.SHIELDBG, Textures.SHIELDFG, 145, shield.health, shield.maxHealth);
    }
  }

  /**
   * Updates the health and shield status bars
   * @param npc
   * @param textures1
   * @param textures2
   * @param y
   * @param health
   * @param maxHealth
   */
  private void updatePositionAndHealth(NPC npc, Textures textures1, Textures textures2, int y, float health, float maxHealth) {
    Vector2 pos = new Vector2(npc.getCenter());
    pos.y += y;
    pos.x -= npc.getWidth() / 2 - 10;
    npc.spriteComponent.getSprite(textures1).setPosition(pos, false);

    SpriteComponent.SpriteItem sprite = npc.spriteComponent.getSprite(textures2);
    sprite.setPosition(pos, false);

    float healthPercentage = health * 100 / maxHealth;
    float healthWidth = sprite.getSprite().getTexture().getWidth() * healthPercentage / 100;
    sprite.setWidth(healthWidth);
  }
}
