package com.starsailor.actors.states.route;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.GuardingNPC;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Route;
import com.starsailor.actors.states.npc.NPCFactory;
import com.starsailor.actors.states.npc.NPCStates;
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

    NPC npc = NPCFactory.createRoutedNPC(shipProfile, route, NPCStates.ROUTE, route.behaviour);

    Gdx.app.log(getClass().getName(), "Route '" + route.getName() + "': spawned ship "
        + route.shipProfile + " at " + routeComponent.spawnPoint.position);

    for(Route.Guard guard : route.guards) {
      GuardingNPC guardingNPC = NPCFactory.createGuardingNPC(guard.ship, npc, guard.behaviour, guard.centeredPosition);
      route.guardingNPCs.add(guardingNPC);
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