package com.starsailor.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.GuardingNPC;
import com.starsailor.managers.SteeringManager;

/**
 * Let the give npc follow its route.
 */
public class GuardState implements State<GuardingNPC> {
  @Override
  public void enter(GuardingNPC npc) {
    SteeringManager.setGuardSteering(npc);
  }

  @Override
  public void update(GuardingNPC npc) {
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
  public void exit(GuardingNPC npc) {

  }

  @Override
  public boolean onMessage(GuardingNPC npc, Telegram telegram) {
    return false;
  }
}
