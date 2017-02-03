package com.starsailor.actors;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.data.ShipProfile;
import com.starsailor.managers.EntityManager;

/**
 * Handling the initialization problem of npc
 */
public class NPCFactory {

  public static RoutedNPC createRoutedNPC(ShipProfile shipProfile, Route route, State state) {
    RoutedNPC npc = new RoutedNPC(shipProfile, route, state);
    npc.createComponents(shipProfile);
    npc.getStateMachine().changeState(state);

    EntityManager.getInstance().add(npc);
    return npc;
  }

  public static GuardingNPC createGuardingNPC(ShipProfile shipProfile, RoutedNPC guardedNPC, State<NPC> state, Vector2 position) {
    GuardingNPC npc = new GuardingNPC(shipProfile, guardedNPC, state, position);
    npc.createComponents(shipProfile);
    npc.getStateMachine().changeState(state);

    guardedNPC.addGuard(npc);

    EntityManager.getInstance().add(npc);
    return npc;
  }
}
