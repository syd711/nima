package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Player;
import com.starsailor.components.ShipDataComponent;
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
        ShipDataComponent dataComponent = npc.getComponent(ShipDataComponent.class);

        if(spriteComponent != null) {
          Vector2 center = npc.getCenter();
          spriteComponent.getSprite(Textures.SELECTION).setPosition(center, true);

          Vector2 hbPos = new Vector2(center);
          hbPos.y+=120;
          hbPos.x-=npc.getWidth()/2-10;
          spriteComponent.getSprite(Textures.HEALTHBG).setPosition(hbPos, false);

          SpriteComponent.SpriteItem sprite = spriteComponent.getSprite(Textures.HEALTHFG);
          sprite.setPosition(hbPos, false);

          float healthPercentage = dataComponent.health*100/dataComponent.maxHealth;
          float healthWidth = sprite.getSprite().getTexture().getWidth()*healthPercentage/100;
          sprite.setWidth(healthWidth);
        }
      }

    }
  }
}