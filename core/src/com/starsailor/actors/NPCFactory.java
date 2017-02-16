package com.starsailor.actors;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.states.npc.GuardState;
import com.starsailor.actors.states.npc.RouteState;
import com.starsailor.actors.states.npc.RoutedSeekAndDestroyState;
import com.starsailor.components.RouteComponent;
import com.starsailor.data.ShipProfile;
import com.starsailor.managers.EntityManager;

/**
 * Handling the initialization problem of npc
 */
public class NPCFactory {

  /**
   * Used for route entities to spawn the hole group
   * @param route
   */
  public static void createRouteNPCs(Route route) {
    RouteComponent routeComponent = route.routeComponent;
    Vector2 position = routeComponent.spawnPoint.position;
    ShipProfile shipProfile = route.shipProfile;
    Fraction fraction = route.fractionComponent.fraction;

    State state = stateForProfile(shipProfile);

    //create route owner first
    NPC routedNPC = new NPC(shipProfile, state, position);
    routedNPC.setRoute(route);
    routedNPC.createComponents(fraction);
    routedNPC.formationComponent.formationOwner = routedNPC;
    routedNPC.getStateMachine().changeState(state);

    EntityManager.getInstance().add(routedNPC);

    //add route members
    for(Route.RouteMember member : route.members) {
      State memberState = stateForProfile(member.shipProfile);
      NPC npc = new NPC(member.shipProfile, memberState, member.position);
      npc.createComponents(fraction);
      npc.formationComponent.formationOwner = routedNPC;

      routedNPC.formationComponent.addMember(npc);
      npc.getStateMachine().changeState(memberState);

      EntityManager.getInstance().add(npc);
    }
  }

  /**
   * Used for wandering ships, therefore we don't have a fix route here
   * @param shipProfile
   * @param state
   * @param fraction
   * @param position
   * @return
   */
  public static NPC createPirate(ShipProfile shipProfile, State state, Fraction fraction, Vector2 position) {
    NPC npc = new NPC(shipProfile, state, position);
    npc.createComponents(fraction);
    npc.formationComponent.formationOwner = npc;
    npc.getStateMachine().changeState(state);

    EntityManager.getInstance().add(npc);
    return npc;
  }

  //------------------- Helper --------------------------------------
  private static State stateForProfile(ShipProfile shipProfile) {
    ShipProfile.Types type = shipProfile.getType();
    switch(type) {
      case PIRATE: {
        return new RoutedSeekAndDestroyState();
      }
      case CRUSADER: {
        return new GuardState();
      }
      default: {
        return new RouteState();
      }
    }
  }
}
