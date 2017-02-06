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
    npc.setShieldEnabled(true);

    ShipProfile.Types type = npc.shipProfile.getType();
    switch(type) {
      case CRUSADER: {
        npc.lockTarget(npc.formationOwner.attacker);
        SteeringManager.setBattleSteering(npc);
        break;
      }
      case MERCHANT: {
        npc.getStateMachine().changeState(new FleeFromAttackerState());
        break;
      }
      case PIRATE: {
        throw new UnsupportedOperationException("Pirate attack not supported yet");
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
