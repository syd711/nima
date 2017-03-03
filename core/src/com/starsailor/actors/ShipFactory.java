package com.starsailor.actors;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.states.npc.GuardState;
import com.starsailor.actors.states.npc.RouteState;
import com.starsailor.actors.states.npc.RoutedSeekAndDestroyState;
import com.starsailor.actors.states.player.PlayerState;
import com.starsailor.managers.EntityManager;
import com.starsailor.managers.GameDataManager;
import com.starsailor.model.ShipData;
import com.starsailor.model.items.ShipItem;

import java.util.List;

/**
 * Handling the initialization problem of npc
 */
public class ShipFactory {

  public static Player createPlayer() {
    ShipData ship = (ShipData) GameDataManager.getInstance().getModel(0);
    Player player = new Player(ship);
    player.createComponents( Fraction.PLAYER);
    player.getStateMachine().changeState(PlayerState.IDLE);
    return player;
  }

  /**
   * Common factory method for creating an npc based on the manager and map data
   * @param shipItem the ship data from the database
   * @param position the position on the map
   * @return
   */
  public static NPC createNPC(ShipItem shipItem, Vector2 position) {
    State state = stateFor(shipItem);
    NPC npc = new NPC(shipItem.getName(), shipItem.getShipData(), state, position);
    npc.createComponents(Fraction.valueOf(shipItem.getFraction().toUpperCase()));
    npc.formationComponent.formationOwner = npc;
    npc.getStateMachine().changeState(state);

    EntityManager.getInstance().add(npc);

    Route route = getRoute(shipItem);
    //TODO

    return npc;
  }


  /**
   * Used for route entities to spawn the hole group
   * @param route
   */
  @Deprecated
  public static void createRouteNPCs(Route route) {
//    RouteComponent routeComponent = route.routeComponent;
//    Vector2 position = routeComponent.spawnPoint.position;
//    ShipData shipData = route.shipData;
//    Fraction fraction = route.fractionComponent.fraction;
//
//    State state = stateFor(shipData);
//
//    //create route owner first
//    NPC routedNPC = new NPC(route.getName(), shipData, state, position);
//    routedNPC.setRoute(route);
//    routedNPC.createComponents(fraction);
//    routedNPC.formationComponent.formationOwner = routedNPC;
//    routedNPC.getStateMachine().changeState(state);
//
//    EntityManager.getInstance().add(routedNPC);
//
//    //add route members
//    for(Route.RouteMember member : route.members) {
//      State memberState = stateFor(member.shipData);
//      String name = member.name;
//
//      NPC npc = new NPC(name, member.shipData, memberState, member.position);
//      npc.createComponents(fraction);
//      npc.formationComponent.formationOwner = routedNPC;
//
//      routedNPC.formationComponent.addMember(npc);
//      npc.getStateMachine().changeState(memberState);
//
//      EntityManager.getInstance().add(npc);
//    }
  }

  //------------------- Helper --------------------------------------

  private static Route getRoute(ShipItem shipItem) {
    if(shipItem.getRoute() != null) {
      List<Route> entities = EntityManager.getInstance().getEntities(Route.class);
      for(Route entity : entities) {
        if(entity.getName().equals(shipItem.getRoute())) {
          return entity;
        }
      }
    }
    return null;
  }

  private static State stateFor(ShipItem shipItem) {
    Steering steering = Steering.valueOf(shipItem.getDefaultSteering().toUpperCase());
    switch(steering) {
      case SEEK_AND_DESTROY: {
        return new RoutedSeekAndDestroyState();
      }
      case GUARD: {
        return new GuardState();
      }
      default: {
        return new RouteState();
      }
    }
  }
}
