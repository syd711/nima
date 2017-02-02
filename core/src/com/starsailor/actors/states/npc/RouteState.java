package com.starsailor.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.*;
import com.badlogic.gdx.ai.steer.limiters.AngularLimiter;
import com.badlogic.gdx.ai.steer.limiters.NullLimiter;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.RoutedNPC;
import com.starsailor.components.RoutingComponent;
import com.starsailor.components.SteerableComponent;
import com.starsailor.util.Box2dRadiusProximity;

import static com.starsailor.Game.world;
import static com.starsailor.util.Settings.MPP;

/**
 * Let the give npc follow its route.
 */
public class RouteState implements State<RoutedNPC> {
  @Override
  public void enter(RoutedNPC npc) {
    SteerableComponent sourceSteering = npc.getComponent(SteerableComponent.class);
    RoutingComponent routingComponent = npc.getComponent(RoutingComponent.class);

    LinePath<Vector2> linePath = new LinePath<>(routingComponent.getWayPoints(), false);
    FollowPath followPathSB = new FollowPath(sourceSteering, linePath, 1)
        .setTimeToTarget(0.1f)
        .setArrivalTolerance(0.01f)
        .setDecelerationRadius(20);


    Box2dRadiusProximity proximity = new Box2dRadiusProximity(sourceSteering, world, sourceSteering.getBoundingRadius() * MPP);
    CollisionAvoidance<Vector2> collisionAvoidanceSB = new CollisionAvoidance<Vector2>(sourceSteering, proximity);

    LookWhereYouAreGoing lookWhereYouAreGoingSB = new LookWhereYouAreGoing<Vector2>(sourceSteering) //
        .setLimiter(new AngularLimiter(100, 20)) //
        .setTimeToTarget(0.1f) //
        .setAlignTolerance(0.001f) //
        .setDecelerationRadius(MathUtils.PI);

    BlendedSteering<Vector2> reachPositionAndOrientationSB = new BlendedSteering<Vector2>(sourceSteering)
        .setLimiter(NullLimiter.NEUTRAL_LIMITER) //
        .add(followPathSB, 1f) //
        .add(collisionAvoidanceSB, 1f) //
        .add(lookWhereYouAreGoingSB, 0.2f);

    sourceSteering.setBehavior(reachPositionAndOrientationSB);
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
