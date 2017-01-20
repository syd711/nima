package com.starsailor.actors.states;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.NPC;

/**
 * The different states of an attack.
 */
public enum MerchantStates implements State<NPC> {
;

  @Override
  public void enter(NPC entity) {

  }

  @Override
  public void update(NPC entity) {

  }

  @Override
  public void exit(NPC entity) {

  }

  @Override
  public boolean onMessage(NPC entity, Telegram telegram) {
    return false;
  }

  // ------------------- Helper -----------------------------------
  private static void updateState(NPC npc) {
//    MerchantStates newState = null;
//    float distance = StateHelper.getDistanceToPlayer(npc);
//    if(distance < 250) {
//      newState = MerchantStates.EVADE;
//    }
//    else if(distance > 250 && distance < 350){
//      newState = MerchantStates.FACE;
//    }
//    else {
//      newState = MerchantStates.PURSUE;
//    }
//
//    if(npc.getStateMachine().getCurrentState() != newState) {
//      npc.getStateMachine().changeState(newState);
//    }
  }
}
