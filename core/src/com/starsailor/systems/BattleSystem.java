package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.physics.box2d.Body;
import com.starsailor.Game;
import com.starsailor.actors.NPC;
import com.starsailor.components.ShieldComponent;
import com.starsailor.components.ShootingComponent;
import com.starsailor.data.BodyData;
import com.starsailor.util.box2d.BodyGenerator;

public class BattleSystem extends PauseableIteratingSystem {
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
   *
   * @param npc
   */
  private void updateShooting(NPC npc) {
//    if(npc.isAggressive()) {
//
//      List<WeaponProfile> weapons = npc.getWeapons();
//      for(WeaponProfile weapon : weapons) {
//        switch(weapon.type) {
//          case LASER: {
//            if(npc.isInShootingRange()) {
//              npc.shootingComponent.setActiveWeaponProfile(weapon);
//              if(npc.shootingComponent.isCharged()) {
//                npc.fireAt(Player.getInstance());
//              }
//            }
//            break;
//          }
//          case MISSILE: {
//            if(npc.isInShootingRange()) {
//              npc.shootingComponent.setActiveWeaponProfile(weapon);
//              if(npc.shootingComponent.isCharged()) {
//                npc.fireAt(Player.getInstance());
//              }
//            }
//            break;
//          }
//        }
//      }
//    }
  }

  /**
   * Updates the shield state for the given NPC
   *
   * @param npc
   */
  private void upateShield(NPC npc) {
    ShieldComponent shieldComponent = npc.getComponent(ShieldComponent.class);
    if(shieldComponent.isActive()) {
      if(npc.shieldComponent.body == null) {
        npc.shieldComponent.body = BodyGenerator.createShieldBody(Game.world, npc, new BodyData());
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