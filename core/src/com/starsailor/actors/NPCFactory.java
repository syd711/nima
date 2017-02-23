package com.starsailor.actors;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.states.npc.GuardState;
import com.starsailor.actors.states.npc.RouteState;
import com.starsailor.actors.states.npc.RoutedSeekAndDestroyState;
import com.starsailor.components.RouteComponent;
import com.starsailor.data.ShipData;
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
    ShipData shipData = route.shipData;
    Fraction fraction = route.fractionComponent.fraction;

    State state = stateForProfile(shipData);

    //create route owner first
    NPC routedNPC = new NPC(route.getName(), shipData, state, position);
    routedNPC.setRoute(route);
    routedNPC.createComponents(fraction);
    routedNPC.formationComponent.formationOwner = routedNPC;
    routedNPC.getStateMachine().changeState(state);

    EntityManager.getInstance().add(routedNPC);

    //add route members
    for(Route.RouteMember member : route.members) {
      State memberState = stateForProfile(member.shipData);
      String name = member.name;

      NPC npc = new NPC(name, member.shipData, memberState, member.position);
      npc.createComponents(fraction);
      npc.formationComponent.formationOwner = routedNPC;

      routedNPC.formationComponent.addMember(npc);
      npc.getStateMachine().changeState(memberState);

      EntityManager.getInstance().add(npc);
    }
  }

  /**
   * Used for wandering ships, therefore we don't have a fix route here
   */
  public static NPC createPirate(String name, ShipData shipData, State state, Fraction fraction, Vector2 position) {
    NPC npc = new NPC(name, shipData, state, position);
    npc.createComponents(fraction);
    npc.formationComponent.formationOwner = npc;
    npc.getStateMachine().changeState(state);

    EntityManager.getInstance().add(npc);
    return npc;
  }

  //------------------- Helper --------------------------------------
  private static State stateForProfile(ShipData shipData) {
    ShipData.Types type = shipData.getType();
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
