package com.nima.actors.states;

import com.badlogic.gdx.ai.fsm.State;
import com.nima.actors.states.npc.*;
import com.nima.actors.states.npc.RouteState;

/**
 *
 */
public class NPCStates {

  public static State IDLE = new IdleState();
  public static State ROUTE = new RouteState();
  public static State EVADE_PLAYER = new EvadePlayerState();
  public static State FACE_PLAYER = new FacePlayerState();
  public static State ROUTE_POINT_ARRIVED = new RoutingPointArrivedState();
  public static State PURSUE_PLAYER = new PursuePlayerState();
}
