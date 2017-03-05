package com.starsailor.actors.states.formation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.FormationOwner;

/**
 *
 */
public class IdleState implements State<FormationOwner> {
  @Override
  public void enter(FormationOwner formationOwner) {
    Gdx.app.log(getClass().getName(), formationOwner + " entered IdleState");
  }

  @Override
  public void update(FormationOwner formationOwner) {
    if(!formationOwner.isInBattleState()) {
      formationOwner.statefulComponent.stateMachine.changeState(new RouteState());
    }
  }

  @Override
  public void exit(FormationOwner formationOwner) {

  }

  @Override
  public boolean onMessage(FormationOwner formationOwner, Telegram telegram) {
    return false;
  }
}
