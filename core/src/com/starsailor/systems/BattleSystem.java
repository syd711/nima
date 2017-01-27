package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.physics.box2d.Body;
import com.starsailor.Game;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Player;
import com.starsailor.components.ShieldComponent;
import com.starsailor.components.ShootingComponent;
import com.starsailor.util.BodyGenerator;

public class BattleSystem extends AbstractIteratingSystem {
  public BattleSystem() {
    super(Family.all(ShootingComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    if(entity instanceof NPC) {
      NPC npc = (NPC) entity;
      updateShooting(npc);
      upateShield(npc);
    }
  }

  /**
   * Fires the available weapons for the given NPC
   * @param npc
   */
  private void updateShooting(NPC npc) {
    if(npc.isAggressive()) {
      if(npc.isInShootingRange() && npc.shootingComponent.isCharged()) {
        npc.fireAt(Player.getInstance());
      }
    }
  }

  /**
   * Updates the shield state for the given NPC
   * @param npc
   */
  private void upateShield(NPC npc) {
    ShieldComponent shieldComponent = npc.getComponent(ShieldComponent.class);
    if(shieldComponent.isActive()) {
      if(npc.shieldComponent.body == null) {
        npc.shieldComponent.body = BodyGenerator.createShieldBody(Game.world, npc);
      }
      Body body = npc.shieldComponent.body;
      body.setUserData(npc);
      body.setTransform(npc.getBox2dCenter(), body.getAngle());
    }
    else {
      if(npc.shieldComponent.body != null) {
        Game.world.destroyBody(npc.shieldComponent.body);
        npc.shieldComponent.body = null;
      }
    }
  }


}