package com.starsailor.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.CollisionAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.LookWhereYouAreGoing;
import com.badlogic.gdx.ai.steer.limiters.AngularLimiter;
import com.badlogic.gdx.ai.steer.limiters.NullLimiter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.GuardingNPC;
import com.starsailor.components.SteerableComponent;
import com.starsailor.util.box2d.Box2dRadiusProximity;

import static com.starsailor.Game.world;
import static com.starsailor.util.Settings.MPP;

/**
 * Let the give npc follow its route.
 */
public class GuardState implements State<GuardingNPC> {
  @Override
  public void enter(GuardingNPC npc) {
    SteerableComponent sourceSteering = npc.getComponent(SteerableComponent.class);

    Arrive<Vector2> arrive = new Arrive<>(sourceSteering, npc.getTargetLocation());
    arrive.setTimeToTarget(0.1f);
    arrive.setArrivalTolerance(2f);
    arrive.setDecelerationRadius(10);

    Box2dRadiusProximity proximity = new Box2dRadiusProximity(sourceSteering, world, sourceSteering.getBoundingRadius() * MPP);
    CollisionAvoidance<Vector2> collisionAvoidanceSB = new CollisionAvoidance<Vector2>(sourceSteering, proximity);

    LookWhereYouAreGoing lookWhereYouAreGoingSB = new LookWhereYouAreGoing<Vector2>(sourceSteering) //
        .setLimiter(new AngularLimiter(0.5f, 0.5f)) //
        .setTimeToTarget(0.1f) //
        .setAlignTolerance(0.001f) //
        .setDecelerationRadius(MathUtils.PI);

    BlendedSteering<Vector2> reachPositionAndOrientationSB = new BlendedSteering<Vector2>(sourceSteering);
    reachPositionAndOrientationSB.setLimiter(NullLimiter.NEUTRAL_LIMITER);
    reachPositionAndOrientationSB.add(arrive, 1f);
    reachPositionAndOrientationSB.add(collisionAvoidanceSB, 1f) ;
    reachPositionAndOrientationSB.add(lookWhereYouAreGoingSB, 0.5f);

    npc.steerableComponent.setBehavior(reachPositionAndOrientationSB);
  }

  @Override
  public void update(GuardingNPC npc) {
//    float distanceToPlayer = npc.getDistanceToPlayer();
//
//    if(npc.isAggressive()) {
//      if(distanceToPlayer < npc.shipProfile.attackDistance) {
//        npc.getStateMachine().changeState(NPCStates.PURSUE_PLAYER);
//      }
//    }
//    else if(distanceToPlayer < npc.shipProfile.evadeDistance) {
//      npc.getStateMachine().changeState(NPCStates.AVOID_PLAYER_COLLISION);
//    }
  }

  @Override
  public void exit(GuardingNPC npc) {

  }

  @Override
  public boolean onMessage(GuardingNPC npc, Telegram telegram) {
    return false;
  }
}
