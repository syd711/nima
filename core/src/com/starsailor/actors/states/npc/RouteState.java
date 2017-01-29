package com.starsailor.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.NPC;
import com.starsailor.components.RoutingComponent;
import com.starsailor.components.SteerableComponent;

/**
 * Let the give npc follow its route.
 */
public class RouteState implements State<NPC> {
  @Override
  public void enter(NPC npc) {
    SteerableComponent sourceSteering = npc.getComponent(SteerableComponent.class);
    RoutingComponent routingComponent = npc.getComponent(RoutingComponent.class);

    LinePath<Vector2> linePath = new LinePath<>(routingComponent.getWayPoints(), false);
    FollowPath followPathSB = new FollowPath(sourceSteering, linePath, 1)
        .setTimeToTarget(0.1f)
        .setArrivalTolerance(0.01f)
        .setDecelerationRadius(20);

    sourceSteering.setBehavior(followPathSB);
  }

  @Override
  public void update(NPC npc) {
    float distanceToPlayer = npc.getDistanceToPlayer();

    if(npc.isAggressive()) {
      if(distanceToPlayer < npc.shipProfile.attackDistance) {
        npc.getStateMachine().changeState(NPCStates.PURSUE_PLAYER);
      }
    }
    else if(distanceToPlayer < npc.shipProfile.evadeDistance) {
      npc.getStateMachine().changeState(NPCStates.AVOID_PLAYER_COLLISION);
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
