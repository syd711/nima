package com.starsailor.actors.states;

import com.badlogic.gdx.ai.fsm.State;
import com.starsailor.actors.states.route.IdleState;
import com.starsailor.actors.states.route.SpawnShipState;

/**
 * Wrapper for all route states
 */
public class RouteStates {
  public static State IDLE = new IdleState();
  public static State INACTIVE = new IdleState();
  public static State SPAWN_SHIP = new SpawnShipState();
}
