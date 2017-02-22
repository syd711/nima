package com.starsailor.actors.states.player;

import com.badlogic.gdx.ai.fsm.State;

/**
 * All states of the player
 */
public class PlayerState {

  public static State FOLLOW_CLICK = new FollowClickState();
  public static State IDLE = new IdleState();
  public static State DOCKED = new DockedState();
  public static State UNDOCK_FROM_STATION = new UndockFromStationState();
  public static State MOVE_TO_STATION = new MoveToStationState();
  public static State DOCK_TO_STATION = new DockToStationState();
}
