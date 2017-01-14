package com.nima.actors.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.nima.actors.NPC;
import com.nima.actors.Route;
import com.nima.components.RouteComponent;
import com.nima.data.ShipProfile;
import com.nima.managers.EntityManager;
import com.nima.render.converters.MapConstants;

/**
 * The different states of an attack.
 */
public enum RouteState implements State<Route> {
  IDLE() {
    @Override
    public void enter(Route route) {

    }

    @Override
    public void update(Route route) {

    }
  },
  SPAWN_SHIP() {
    @Override
    public void enter(Route route) {
      RouteComponent routeComponent = route.routeComponent;
      ShipProfile shipProfile = routeComponent.shipProfile;
      NPC npc = new NPC(shipProfile, route, NPCState.IDLE);
      EntityManager.getInstance().add(npc);
      Vector2 spawnPoint = routeComponent.spawnPoint.position;
      Gdx.app.log(getClass().getName(), "Route '" + routeComponent.name + "': spawned ship " + routeComponent.shipProfile + " at " + spawnPoint);

      //TODO behaviour to enum
      if(routeComponent.behaviour.equals(MapConstants.BEHAVIOUR_AGGRESSIVE)) {
        npc.getStateMachine().changeState(NPCState.ROUTE_AGGRESSIVE);
      }
      else {
        npc.getStateMachine().changeState(NPCState.ROUTE);
      }

    }

    @Override
    public void update(Route entity) {
      updateState(entity);
    }
  };

  @Override
  public void enter(Route entity) {

  }

  @Override
  public void exit(Route entity) {

  }

  @Override
  public boolean onMessage(Route entity, Telegram telegram) {
    return false;
  }

  // ------------------- Helper -----------------------------------
  private static void updateState(Route route) {

  }
}
