package com.starsailor.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.Game;
import com.starsailor.actors.NPC;
import com.starsailor.managers.SteeringManager;

/**
 *
 */
public class SeekAndDestroyState implements State<NPC> {
  @Override
  public void enter(NPC npc) {
//    RoutingComponent routingComponent = npc.getComponent(RoutingComponent.class);
//    Array<Vector2> wayPoints = routingComponent.getWayPoints();
//    SteeringManager.setRouteSteering(npc, wayPoints);
    Game.wanderSB = SteeringManager.setWanderSteering(npc);
  }

  @Override
  public void update(NPC npc) {
//    float attackDistance = npc.shipProfile.attackDistance;
//    Ship nearestNeighbour = npc.findNearestNeighbour();
//    if(nearestNeighbour != null) {
//      float distanceToEnemy = nearestNeighbour.getDistanceTo(npc);
//      if(distanceToEnemy != 0 && distanceToEnemy < attackDistance) {
//        ShieldComponent component = npc.getComponent(ShieldComponent.class);
//        component.setActive(true);
//        npc.lockTarget(nearestNeighbour);
//
//        SteeringManager.setBattleSteering(npc);
//      }
//    }
  }

  @Override
  public void exit(NPC npc) {

  }

  @Override
  public boolean onMessage(NPC npc, Telegram telegram) {
    return false;
  }
}
