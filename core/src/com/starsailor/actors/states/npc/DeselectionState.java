package com.starsailor.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.NPC;
import com.starsailor.components.SpriteComponent;

/**
 *
 */
public class DeselectionState implements State<NPC> {
  @Override
  public void enter(NPC npc) {
    npc.selectionComponent.setSelected(false);
    npc.remove(SpriteComponent.class);
  }

  @Override
  public void update(NPC npc) {
    npc.getStateMachine().changeState(npc.getStateMachine().getPreviousState());
  }

  @Override
  public void exit(NPC npc) {

  }

  @Override
  public boolean onMessage(NPC npc, Telegram telegram) {
    return false;
  }
}
