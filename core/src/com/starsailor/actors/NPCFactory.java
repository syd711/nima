package com.starsailor.actors;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.data.ShipProfile;
import com.starsailor.managers.EntityManager;

/**
 * Handling the initialization problem of npc
 */
public class NPCFactory {

  public static NPC createRoutedNPC(ShipProfile shipProfile, Route route, State state, Vector2 position) {
    NPC npc = new NPC(shipProfile, route, state, position);
    npc.createComponents(shipProfile, route.fractionComponent.fraction);
    npc.getStateMachine().changeState(state);

    EntityManager.getInstance().add(npc);
    return npc;
  }

  public static NPC createRouteMember(ShipProfile shipProfile, NPC routeOwner, State<NPC> state, Vector2 position) {
    NPC npc = new NPC(shipProfile, routeOwner, state, position);
    npc.createComponents(shipProfile, routeOwner.fractionComponent.fraction);
    npc.getStateMachine().changeState(state);

    routeOwner.addFormationMember(npc);

    EntityManager.getInstance().add(npc);
    return npc;
  }

  public static NPC createPirate(ShipProfile shipProfile, State state, Fraction fraction, Vector2 position) {
    NPC npc = new NPC(shipProfile, state, position);
    npc.createComponents(shipProfile, fraction);
    npc.getStateMachine().changeState(state);

    EntityManager.getInstance().add(npc);
    return npc;
  }
}
