package com.starsailor.actors.states.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Ship;
import com.starsailor.components.RoutingComponent;
import com.starsailor.managers.SteeringManager;

/**
 * Let the give npc follow its route.
 */
public class RouteState extends NPCState implements State<NPC> {
  @Override
  public void enter(NPC npc) {
    Gdx.app.log(getClass().getName(), npc + " entered RouteState");

    RoutingComponent routingComponent = npc.getComponent(RoutingComponent.class);
    Array<Vector2> wayPoints = routingComponent.getWayPoints();
    SteeringManager.setRouteSteering(npc, wayPoints);
  }

  @Override
  public void update(NPC npc) {
    Ship nearestEnemy = npc.findNearestEnemy(true);
    if(nearestEnemy != null && isInAttackingDistance(npc, nearestEnemy)) {
      npc.switchGroupToBattleState(nearestEnemy);
    }
  }

  @Override
  public void exit(NPC npc) {

  }

  @Override
  public boolean onMessage(NPC npc, Telegram telegram) {
    return false;
  }
}
