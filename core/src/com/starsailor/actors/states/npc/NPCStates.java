package com.starsailor.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;

/**
 *
 */
public class NPCStates {

  public static State IDLE = new NPCIdleState();
  public static State ROUTE_POINT_ARRIVED = new RoutingPointArrivedState();
  public static State ROUTE_STATE = new RouteState();
  public static State TRADING_STATE = new NPCTradingState();
}
