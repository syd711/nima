package com.starsailor.actors.states.route;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.NPC;
import com.starsailor.actors.NPCFactory;
import com.starsailor.actors.Route;
import com.starsailor.actors.states.npc.RouteState;
import com.starsailor.actors.states.npc.RoutedSeekAndDestroyState;
import com.starsailor.components.RouteComponent;
import com.starsailor.data.ShipProfile;

/**
 *
 */
public class SpawnShipsState implements State<Route> {
  @Override
  public void enter(Route route) {
    RouteComponent routeComponent = route.routeComponent;
    ShipProfile shipProfile = route.shipProfile;

    State state = new RouteState();
    //TODO
    if(shipProfile.spine.equalsIgnoreCase("pirate")) {
      state = new RoutedSeekAndDestroyState();
    }

    NPC npc = NPCFactory.createRoutedNPC(shipProfile, route, state, routeComponent.spawnPoint.position);

    Gdx.app.log(getClass().getName(), "Route '" + route.getName() + "': spawned ship "
        + route.shipProfile + " at " + routeComponent.spawnPoint.position);

    for(Route.RouteMember member : route.members) {
      NPC routeMember = NPCFactory.createRouteMember(member.ship, npc, member.state, member.centeredPosition);
      route.routeMembers.add(routeMember);
    }
  }

  @Override
  public void update(Route entity) {

  }

  @Override
  public void exit(Route entity) {

  }

  @Override
  public boolean onMessage(Route entity, Telegram telegram) {
    return false;
  }

  //-------------------- Helper -----------------------------

}
