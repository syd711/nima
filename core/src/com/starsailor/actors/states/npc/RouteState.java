package com.starsailor.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.starsailor.actors.RoutedNPC;
import com.starsailor.components.RoutingComponent;
import com.starsailor.managers.SteeringManager;

/**
 * Let the give npc follow its route.
 */
public class RouteState implements State<RoutedNPC> {
  @Override
  public void enter(RoutedNPC npc) {
    RoutingComponent routingComponent = npc.getComponent(RoutingComponent.class);
    Array<Vector2> wayPoints = routingComponent.getWayPoints();
    SteeringManager.setRouteSteering(npc, wayPoints);
  }

  @Override
  public void update(RoutedNPC npc) {
//    npc.updateFormation();
//    float distanceToPlayer = npc.getDistanceToPlayer();
//
//    if(npc.isAggressive()) {
//      if(distanceToPlayer < npc.shipProfile.attackDistance) {
//        npc.getStateMachine().changeState(RoutedNPCStates.PURSUE_PLAYER);
//      }
//    }
//    else if(distanceToPlayer < npc.shipProfile.evadeDistance) {
//      npc.getStateMachine().changeState(RoutedNPCStates.AVOID_PLAYER_COLLISION);
//    }
  }

  @Override
  public void exit(RoutedNPC npc) {

  }

  @Override
  public boolean onMessage(RoutedNPC npc, Telegram telegram) {
    return false;
  }
}
