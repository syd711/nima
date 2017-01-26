package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Player;
import com.starsailor.components.ShieldComponent;
import com.starsailor.components.ShootingComponent;
import com.starsailor.components.SpriteComponent;
import com.starsailor.managers.Textures;

public class NPCSystem extends AbstractIteratingSystem {
  public NPCSystem() {
    super(Family.all(ShootingComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    if(entity instanceof NPC) {
      NPC npc = (NPC) entity;
      if(npc.isAggressive()) {
        if(npc.isInShootingRange() && npc.shootingComponent.isCharged()) {
          npc.fireAt(Player.getInstance());
        }
      }

      //update selection sprite
      if(npc.selectionComponent.selected) {
        SpriteComponent spriteComponent = npc.getComponent(SpriteComponent.class);
        ShieldComponent shieldComponent = npc.getComponent(ShieldComponent.class);

        if(spriteComponent != null) {
          //update selection sprite
          Vector2 center = npc.getCenter();
          spriteComponent.getSprite(Textures.SELECTION).setPosition(center, true);

          //update health sprites
          updateHealthSprites(npc, spriteComponent, shieldComponent, center);
        }
      }

    }
  }

  private void updateHealthSprites(NPC npc, SpriteComponent spriteComponent, ShieldComponent shieldComponent, Vector2 center) {
    Vector2 hbPos = new Vector2(center);
    hbPos.y+=120;
    hbPos.x-=npc.getWidth()/2-10;
    spriteComponent.getSprite(Textures.HEALTHBG).setPosition(hbPos, false);

    SpriteComponent.SpriteItem sprite = spriteComponent.getSprite(Textures.HEALTHFG);
    sprite.setPosition(hbPos, false);

    float healthPercentage = npc.health*100/npc.maxHealth;
    float healthWidth = sprite.getSprite().getTexture().getWidth()*healthPercentage/100;
    sprite.setWidth(healthWidth);

    if(shieldComponent != null) {
      if(shieldComponent.isActive()) {
        //update shield sprite
        Vector2 shieldPos = new Vector2(center);
        shieldPos.y+=142;
        shieldPos.x-=npc.getWidth()/2-10;
        spriteComponent.getSprite(Textures.SHIELDBG).setPosition(shieldPos, false);

        sprite = spriteComponent.getSprite(Textures.SHIELDFG);
        sprite.setPosition(shieldPos, false);

        float shieldPercentage = shieldComponent.health*100/shieldComponent.maxHealth;
        float shieldWidth = sprite.getSprite().getTexture().getWidth()*shieldPercentage/100;
        sprite.setWidth(shieldWidth);
      }
      else {
        spriteComponent.getSprite(Textures.SHIELDBG).setActive(false);
        spriteComponent.getSprite(Textures.SHIELDFG).setActive(false);
      }
    }
  }
}