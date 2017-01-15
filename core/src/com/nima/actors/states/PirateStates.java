package com.nima.actors.states;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.nima.actors.NPC;

/**
 * States for pirates
 */
public enum PirateStates implements State<NPC> {
  IDLE() {
    @Override
    public void update(NPC npc) {

    }
  },
  ROUTE() {
    @Override
    public void enter(NPC npc) {
      StateHelper.route(npc);
    }

    @Override
    public void update(NPC npc) {
      if(shouldAttack(npc)) {
        npc.getStateMachine().changeState(PURSUE);
      }
    }
  },
  PURSUE() {
    @Override
    public void enter(NPC npc) {
      StateHelper.pursuePlayer(npc);
    }

    @Override
    public void update(NPC npc) {
      //updateState(npc);
    }
  },;

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

  // ------------------- Helper ---------------------------------
  private static boolean shouldAttack(NPC npc) {
    float distance = StateHelper.getDistanceToPlayer(npc);
    return distance < 200;
  }

}
