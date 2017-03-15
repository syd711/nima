package com.starsailor.systems;

import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.physics.box2d.Body;
import com.starsailor.Game;
import com.starsailor.actors.GameEntity;
import com.starsailor.actors.NPC;
import com.starsailor.components.ShieldStatusComponent;
import com.starsailor.components.ShootingComponent;
import com.starsailor.model.BodyData;
import com.starsailor.util.box2d.BodyGenerator;

import static com.starsailor.util.Settings.MPP;

public class BattleSystem extends PauseableIteratingSystem {
  public BattleSystem() {
    super(Family.all(ShootingComponent.class).get());
  }

  public void process(GameEntity entity, float deltaTime) {
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
  }

  /**
   * Updates the shield state for the given NPC
   *
   * @param npc
   */
  private void upateShield(NPC npc) {
    ShieldStatusComponent shieldStatusComponent = npc.getComponent(ShieldStatusComponent.class);
    if(shieldStatusComponent.isActive()) {
      if(npc.shieldBodyComponent.body == null) {
        npc.shieldBodyComponent.body = BodyGenerator.createShieldBody(Game.world, npc, new BodyData());
      }
      Body body = npc.shieldBodyComponent.body;
      body.setUserData(npc);
      body.setTransform(npc.getCenter().scl(MPP), body.getAngle());
    }
    else {
      if(npc.shieldBodyComponent.body != null) {
        Game.world.destroyBody(npc.shieldBodyComponent.body);
        npc.shieldBodyComponent.body = null;
      }
    }
  }


}