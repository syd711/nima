package com.starsailor.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.starsailor.render.converters.MapConstants;

/**
 *
 */
public class NPCStates {

  public static State IDLE = new IdleState();
  public static State ROUTE_POINT_ARRIVED = new RoutingPointArrivedState();

  /**
   * States created via map object constants
   * @param defaultStateName
   * @return
   */
  public static State forName(String defaultStateName) {
    if(defaultStateName.equals(MapConstants.STATE_GUARD)) {
      return new GuardState();
    }
    else if(defaultStateName.equals(MapConstants.STATE_ROUTE)) {
      return new RouteState();
    }
    else if(defaultStateName.equals(MapConstants.STATE_SEEK_AND_DESTROY)) {
      return new WanderingSeekAndDestroyState();
    }

    throw new UnsupportedOperationException("No state found for string " + defaultStateName);
  }
}
