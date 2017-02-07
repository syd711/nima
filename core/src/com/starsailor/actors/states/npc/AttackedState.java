package com.starsailor.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.NPC;
import com.starsailor.data.ShipProfile;
import com.starsailor.managers.SteeringManager;

/**
 *
 */
public class AttackedState implements State<NPC> {
  @Override
  public void enter(NPC npc) {
    boolean attacked = npc.formationOwner.attacking != null;
    npc.setShieldEnabled(attacked);

    //attack is finished, return back to previous state
    if(!attacked) {
      npc.getStateMachine().changeState(npc.getDefaultState());
      return;
    }

    ShipProfile.Types type = npc.shipProfile.getType();
    switch(type) {
      case CRUSADER: {
        npc.lockTarget(npc.formationOwner.attacking);
        SteeringManager.setBattleSteering(npc);
        npc.getStateMachine().changeState(new AttackState());
        break;
      }
      case MERCHANT: {
        npc.getStateMachine().changeState(new FleeFromAttackerState());
        break;
      }
      case PIRATE: {
        npc.getStateMachine().changeState(new AttackState());
        break;
      }
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
