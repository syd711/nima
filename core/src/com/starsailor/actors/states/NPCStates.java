package com.starsailor.actors.states;

import com.badlogic.gdx.ai.fsm.State;
import com.starsailor.actors.states.npc.*;
import com.starsailor.actors.states.npc.RouteState;

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
  public static State AVOID_PLAYER_COLLISION= new AvoidCollisionState();
  public static State SELECT= new SelectionState();
  public static State DESELECT= new DeselectionState();
}
