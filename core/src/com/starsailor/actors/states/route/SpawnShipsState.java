package com.starsailor.actors.states.route;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Route;
import com.starsailor.actors.states.npc.NPCStates;
import com.starsailor.components.RouteComponent;
import com.starsailor.data.ShipProfile;
import com.starsailor.managers.EntityManager;

/**
 *
 */
public class SpawnShipsState implements State<Route> {
  @Override
  public void enter(Route route) {
    RouteComponent routeComponent = route.routeComponent;
    ShipProfile shipProfile = routeComponent.shipProfile;

    NPC npc = new NPC(shipProfile, route, NPCStates.IDLE, routeComponent.behaviour);
    routeComponent.npc = npc;
    EntityManager.getInstance().add(npc);

    Gdx.app.log(getClass().getName(), "Route '" + routeComponent.name + "': spawned ship "
        + routeComponent.shipProfile + " at " + routeComponent.spawnPoint.position);
    npc.getStateMachine().changeState(NPCStates.ROUTE);

    for(RouteComponent.Guard guard : routeComponent.guards) {
      NPC guardingNPC = new NPC(guard.ship, npc, NPCStates.IDLE, guard.behaviour, guard.centeredPosition);
      EntityManager.getInstance().add(guardingNPC);
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
