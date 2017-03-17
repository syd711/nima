package com.starsailor.systems;

import com.badlogic.ashley.core.Family;
import com.starsailor.actors.GameEntity;
import com.starsailor.actors.Ship;
import com.starsailor.components.ShipBodyComponent;

public class BattleSystem extends PauseableIteratingSystem {
  public BattleSystem() {
    super(Family.all(ShipBodyComponent.class).get());
  }

  public void process(GameEntity entity, float deltaTime) {
    Ship ship = (Ship) entity;
    ship.shipBodyComponent.updateBody();
  }

  /**
   * Updates the shield state for the given NPC
   *
   * @param npc
   */
//  private void upateShield(NPC npc) {
//    ShieldStatusComponent shieldStatusComponent = npc.getComponent(ShieldStatusComponent.class);
//    if(shieldStatusComponent.isActive()) {
//      if(npc.shieldBodyComponent.body == null) {
//        npc.shieldBodyComponent.body = BodyGenerator.createShieldBody(Game.world, npc, new BodyData());
//      }
//      Body body = npc.shieldBodyComponent.body;
//      body.setUserData(npc);
//      body.setTransform(npc.getCenter().scl(MPP), body.getAngle());
//    }
//    else {
//      if(npc.shieldBodyComponent.body != null) {
//        Game.world.destroyBody(npc.shieldBodyComponent.body);
//        npc.shieldBodyComponent.body = null;
//      }
//    }
//  }


}