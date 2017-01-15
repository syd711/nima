package com.nima.actors.states;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.nima.actors.NPC;

/**
 * The different states of an attack.
 */
public enum MerchantStates implements State<NPC> {

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
    public void update(NPC entity) {
      //check route state
    }
  },
  ROUTE_POINT_ARRIVED() {
    @Override
    public void update(NPC npc) {
      State previousState = npc.getStateMachine().getPreviousState();
      npc.getStateMachine().changeState(previousState);
    }
  },
  PURSUE() {
    @Override
    public void enter(NPC npc) {
      StateHelper.pursuePlayer(npc);
    }

    @Override
    public void update(NPC npc) {
      updateState(npc);
    }
  },
  FACE() {
    @Override
    public void enter(NPC npc) {
      StateHelper.facePlayer(npc);
    }

    @Override
    public void update(NPC npc) {
      updateState(npc);
    }
  },
  EVADE() {
    @Override
    public void enter(NPC npc) {
      StateHelper.evadePlayer(npc);
    }

    @Override
    public void update(NPC entity) {
      updateState(entity);
    }
  };

  @Override
  public void enter(NPC entity) {

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
    MerchantStates newState = null;
    float distance = StateHelper.getDistanceToPlayer(npc);
    if(distance < 250) {
      newState = MerchantStates.EVADE;
    }
    else if(distance > 250 && distance < 350){
      newState = MerchantStates.FACE;
    }
    else {
      newState = MerchantStates.PURSUE;
    }

    if(npc.getStateMachine().getCurrentState() != newState) {
      npc.getStateMachine().changeState(newState);
    }
  }

  private static boolean shouldAttack(NPC npc) {
    float distance = StateHelper.getDistanceToPlayer(npc);
    return distance < 200;
  }

}
