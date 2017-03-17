package com.starsailor.actors;

import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.route.Route;
import com.starsailor.actors.states.player.PlayerState;
import com.starsailor.managers.EntityManager;
import com.starsailor.managers.GameDataManager;
import com.starsailor.model.items.ShipItem;

import java.util.List;

/**
 * Handling the initialization problem of npc
 */
public class ShipFactory {
  private final static int PLAYER_ID = 35013;

  public static Player createPlayer() {
    ShipItem ship = (ShipItem) GameDataManager.getInstance().getModel(PLAYER_ID);
    Player player = new Player(ship, new Vector2(0, 0));
    player.createComponents();
    player.getStateMachine().changeState(PlayerState.IDLE);
    return player;
  }

  /**
   * Common factory method for creating an npc based on the manager and map data
   *
   * @param shipItem the ship data from the database
   * @param position the position on the map
   * @return
   */
  public static NPC createNPC(ShipItem shipItem, Vector2 position) {
    NPC npc = new NPC(shipItem, position);
    Route route = getRoute(shipItem);
    if(route != null) {
      npc.setRoute(route);
    }
    npc.createComponents();
    return npc;
  }


  /**
   * Used for route entities to spawn the hole group
   *
   * @param route
   */
  //TODO
  @Deprecated
  public static void createRouteNPCs(com.starsailor.actors.route.Route route) {
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

  private static com.starsailor.actors.route.Route getRoute(ShipItem shipItem) {
    if(shipItem.getRoute() != null) {
      List<com.starsailor.actors.route.Route> entities = EntityManager.getInstance().getEntities(Route.class);
      for(com.starsailor.actors.route.Route entity : entities) {
        if(entity.getName().equals(shipItem.getRoute())) {
          return entity;
        }
      }
    }
    return null;
  }
}
