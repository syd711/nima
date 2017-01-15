package com.nima.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.nima.actors.NPC;

/**
 *
 */
public class RoutingPointArrivedState implements State<NPC> {
  @Override
  public void enter(NPC npc) {

  }

  @Override
  public void update(NPC npc) {
    State previousState = npc.getStateMachine().getPreviousState();
    npc.getStateMachine().changeState(previousState);
  }

  @Override
  public void exit(NPC npc) {

  }

  @Override
  public boolean onMessage(NPC npc, Telegram telegram) {
    return false;
  }
}
