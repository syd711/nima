package com.starsailor.actors.states.route;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.ShipFactory;
import com.starsailor.actors.Route;

/**
 *
 */
public class SpawnShipsState implements State<Route> {
  @Override
  public void enter(Route route) {
    ShipFactory.createRouteNPCs(route);
  }

  @Override
  public void update(Route entity) {
    //TODO check respawn
    //TODO check idle
  }

  @Override
  public void exit(Route entity) {

  }

  @Override
  public boolean onMessage(Route entity, Telegram telegram) {
    return false;
  }
}
