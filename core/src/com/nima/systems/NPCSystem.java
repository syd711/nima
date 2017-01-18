package com.nima.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector2;
import com.nima.actors.NPC;
import com.nima.actors.Player;
import com.nima.components.ShootingComponent;

public class NPCSystem extends AbstractIteratingSystem {
  public NPCSystem() {
    super(Family.all(ShootingComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    if(entity instanceof NPC) {
      NPC npc = (NPC) entity;
      if(npc.isAggressive()) {
        if(npc.isInShootingRange() && npc.shootingComponent.isCharged()) {
          Vector2 shootAt = Player.getInstance().getCenter();
          npc.fireAt(shootAt);
        }
      }
    }
  }
}