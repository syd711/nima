package com.starsailor.actors.states.formation;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.FormationOwner;

/**
 *
 */
public class IdleState implements State<FormationOwner> {
  @Override
  public void enter(FormationOwner npc) {

  }

  @Override
  public void update(FormationOwner npc) {

  }

  @Override
  public void exit(FormationOwner npc) {

  }

  @Override
  public boolean onMessage(FormationOwner npc, Telegram telegram) {
    return false;
  }
}
