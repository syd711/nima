package com.starsailor.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.behaviors.Cohesion;
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.starsailor.actors.GuardingNPC;
import com.starsailor.actors.Player;
import com.starsailor.components.SteerableComponent;

/**
 * Let the give npc follow its route.
 */
public class GuardState implements State<GuardingNPC> {
  @Override
  public void enter(GuardingNPC npc) {
    SteerableComponent sourceSteering = npc.getComponent(SteerableComponent.class);
    SteerableComponent targetSteering = npc.guardedNPC.getComponent(SteerableComponent.class);


    float radius = 1f;
    Array<Steerable<Vector2>> obstacles = new Array<>();
    obstacles.add(Player.getInstance().steerableComponent);
    RadiusProximity<Vector2> radiusProximity = new RadiusProximity<>(targetSteering, obstacles, radius);
    Cohesion<Vector2> behaviour = new Cohesion<>(sourceSteering, radiusProximity);
    sourceSteering.setBehavior(behaviour);

//    BlendedSteering<Vector2> blendedSteering = new BlendedSteering<Vector2>() //
//        .add(groupAlignmentSB, .2f) //
//        .add(groupCohesionSB, .06f) //
//        .add(groupSeparationSB, 1.7f);
//    blendedSteerings.add(blendedSteering);
  }

  @Override
  public void update(GuardingNPC npc) {
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
  public void exit(GuardingNPC npc) {

  }

  @Override
  public boolean onMessage(GuardingNPC npc, Telegram telegram) {
    return false;
  }
}
