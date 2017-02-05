package com.starsailor.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.NPC;
import com.starsailor.data.WeaponProfile;
import com.starsailor.managers.SteeringManager;

import java.util.List;

/**
 *
 */
public class AttackedState implements State<NPC> {
  @Override
  public void enter(NPC npc) {
    npc.setShieldEnabled(true);

    //apply target
    if(npc.attacker != null) {
      npc.lockTarget(npc.attacker);
      SteeringManager.setBattleSteering(npc);
    }
    else {
      npc.steerableComponent.setBehavior(null);
    }
  }

  @Override
  public void update(NPC npc) {
    List<WeaponProfile> weapons = npc.getWeapons();

  }

  @Override
  public void exit(NPC npc) {

  }

  @Override
  public boolean onMessage(NPC npc, Telegram telegram) {
    return false;
  }
}
