package com.starsailor.actors.states;

import com.badlogic.gdx.ai.fsm.State;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Steering;
import com.starsailor.actors.states.npc.*;

/**
 *
 */
public class StateFactory {

  public static State<NPC> createState(Steering steering) {
    switch(steering) {
      case SEEK_AND_DESTROY: {
        return new RoutedSeekAndDestroyState();
      }
      case WANDERING_SEEK_AND_DESTROY: {
        return new WanderingSeekAndDestroyState();
      }
      case ROUTE: {
        return new RouteState();
      }
      case ATTACK: {
        return new AttackStateNPC();
      }
      case FLEE: {
        return new FleeFromAttackerAndWaitStateNPC();
      }
      default: {
        return new RouteState();
      }
    }
  }
}
