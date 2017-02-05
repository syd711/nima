package com.starsailor.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.NPC;
import com.starsailor.managers.SteeringManager;

/**
 * Let the give npc follow its route.
 */
public class GuardState implements State<NPC> {
  @Override
  public void enter(NPC npc) {
    SteeringManager.setGuardSteering(npc);
  }

  @Override
  public void update(NPC npc) {
//    float distanceToPlayer = npc.getDistanceToPlayer();
//
//    if(npc.isAggressive()) {
//      if(distanceToPlayer < npc.shipProfile.attackDistance) {
//        npc.getStateMachine().changeState(NPCStates.PURSUE_PLAYER);
//      }
//    }
//    else if(distanceToPlayer < npc.shipProfile.evadeDistance) {
//      npc.getStateMachine().changeState(NPCStates.AVOID_PLAYER_COLLISION);
//    }
  }

  @Override
  public void exit(NPC npc) {

  }

  @Override
  public boolean onMessage(NPC npc, Telegram telegram) {
    return false;
  }
}
