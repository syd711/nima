package com.starsailor.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.NPC;
import com.starsailor.data.WeaponProfile;

import java.util.List;

/**
 *
 */
public class AttackState implements State<NPC> {
  @Override
  public void enter(NPC npc) {
  }

  @Override
  public void update(NPC npc) {
    if(npc.attacking == null || npc.formationOwner.attacking == null) {
      npc.getStateMachine().changeState(npc.getStateMachine().getPreviousState());
      return;
    }

    List<WeaponProfile> weapons = npc.getWeapons();
    for(WeaponProfile weapon : weapons) {
      switch(weapon.type) {
        case LASER: {
          npc.shootingComponent.setActiveWeaponProfile(weapon);
          if(npc.shootingComponent.isCharged()) {
            npc.fireAtTarget();
          }
          break;
        }
        case MISSILE: {
          npc.shootingComponent.setActiveWeaponProfile(weapon);
          if(npc.shootingComponent.isCharged()) {
            npc.fireAtTarget();
          }
          break;
        }
      }
    }
  }

  @Override
  public void exit(NPC npc) {
    System.out.println("Finished attack");
  }

  @Override
  public boolean onMessage(NPC npc, Telegram telegram) {
    return false;
  }
}
