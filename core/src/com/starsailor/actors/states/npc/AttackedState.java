package com.starsailor.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.NPC;
import com.starsailor.managers.SteeringManager;

/**
 *
 */
public class AttackedState implements State<NPC> {
  @Override
  public void enter(NPC npc) {
    npc.setShieldEnabled(true);

    //TODO
    if(npc.shipProfile.spine.equalsIgnoreCase("merchant")) {
      npc.getStateMachine().changeState(new FleeFromAttackerState());
      return;
    }

    //apply target
    if(npc.formationOwner.attacker != null) {
      npc.lockTarget(npc.formationOwner.attacker);
      SteeringManager.setBattleSteering(npc);
    }
    else {
      npc.steerableComponent.setBehavior(null);
    }
  }

  @Override
  public void update(NPC npc) {
  }

  @Override
  public void exit(NPC npc) {
  }

  @Override
  public boolean onMessage(NPC npc, Telegram telegram) {
    return false;
  }
}
