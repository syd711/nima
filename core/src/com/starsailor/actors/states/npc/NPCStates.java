package com.starsailor.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;

/**
 *
 */
public class NPCStates {

  public static State IDLE = new IdleState();
  public static State ROUTE_POINT_ARRIVED = new RoutingPointArrivedState();
}
