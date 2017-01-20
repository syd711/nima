package com.starsailor.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.NPC;
import com.starsailor.actors.states.NPCStates;
import com.starsailor.components.RoutingComponent;
import com.starsailor.components.SteerableComponent;
import com.starsailor.actors.RoutePoint;

import static com.starsailor.render.converters.MapConstants.BEHAVIOUR_AGGRESSIVE;

/**
 * Let the give npc follow its route.
 */
public class RouteState implements State<NPC> {
  @Override
  public void enter(NPC npc) {
    SteerableComponent sourceSteering = npc.getComponent(SteerableComponent.class);
    RoutingComponent routingComponent = npc.getComponent(RoutingComponent.class);

    RoutePoint point = routingComponent.nextTarget();
    SteerableComponent targetSteering = routingComponent.getSteeringComponent(point);

    Arrive<Vector2> behaviour = new Arrive<>(sourceSteering, targetSteering);
    behaviour.setArrivalTolerance(0.10f);
    behaviour.setDecelerationRadius(1f);
    sourceSteering.setBehavior(behaviour);
    sourceSteering.setFaceBehaviour(null);
  }

  @Override
  public void update(NPC npc) {
    String behaviour = npc.getBehaviour();
    float distanceToPlayer = npc.getDistanceToPlayer();

    if(behaviour.equals(BEHAVIOUR_AGGRESSIVE)) {
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
