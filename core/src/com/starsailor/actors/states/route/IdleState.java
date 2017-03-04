package com.starsailor.actors.states.route;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.route.Route;

/**
 *
 */
public class IdleState implements State<Route> {
  @Override
  public void enter(Route entity) {

  }

  @Override
  public void update(Route entity) {

  }

  @Override
  public void exit(Route entity) {

  }

  @Override
  public boolean onMessage(Route entity, Telegram telegram) {
    return false;
  }
}
