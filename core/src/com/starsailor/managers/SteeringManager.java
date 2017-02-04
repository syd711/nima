package com.starsailor.managers;

import com.badlogic.gdx.ai.steer.behaviors.*;
import com.badlogic.gdx.ai.steer.limiters.AngularLimiter;
import com.badlogic.gdx.ai.steer.limiters.LinearAccelerationLimiter;
import com.badlogic.gdx.ai.steer.limiters.NullLimiter;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.starsailor.actors.GuardingNPC;
import com.starsailor.actors.NPC;
import com.starsailor.components.SteerableComponent;
import com.starsailor.util.box2d.Box2dRadiusProximity;

import static com.starsailor.Game.world;
import static com.starsailor.util.Settings.MPP;

/**
 * Used to apply steering behaviours to entities
 */
public class SteeringManager {

  public static void setGuardSteering(GuardingNPC npc) {
    SteerableComponent sourceSteering = npc.getComponent(SteerableComponent.class);

    Arrive<Vector2> arrive = new Arrive<>(sourceSteering, npc.getTargetLocation());
    arrive.setTimeToTarget(0.1f);
    arrive.setArrivalTolerance(0.2f);
    arrive.setDecelerationRadius(10);

    Box2dRadiusProximity proximity = new Box2dRadiusProximity(sourceSteering, world, sourceSteering.getBoundingRadius() * MPP);
    CollisionAvoidance<Vector2> collisionAvoidanceSB = new CollisionAvoidance<Vector2>(sourceSteering, proximity);

    LookWhereYouAreGoing lookWhereYouAreGoingSB = new LookWhereYouAreGoing<Vector2>(sourceSteering);
    lookWhereYouAreGoingSB.setLimiter(new AngularLimiter(sourceSteering.getMaxAngularAcceleration(), sourceSteering.getMaxAngularSpeed()));
    lookWhereYouAreGoingSB.setTimeToTarget(0.1f);
    lookWhereYouAreGoingSB.setAlignTolerance(0.001f);
    lookWhereYouAreGoingSB.setDecelerationRadius(MathUtils.PI);

    BlendedSteering<Vector2> reachPositionAndOrientationSB = new BlendedSteering<Vector2>(sourceSteering);
    reachPositionAndOrientationSB.setLimiter(NullLimiter.NEUTRAL_LIMITER);
    reachPositionAndOrientationSB.add(arrive, 1f);
    reachPositionAndOrientationSB.add(collisionAvoidanceSB, 1f);
    reachPositionAndOrientationSB.add(lookWhereYouAreGoingSB, 0.5f);

    npc.steerableComponent.setBehavior(reachPositionAndOrientationSB);
  }

  public static void setRouteSteering(NPC npc, Array<Vector2> wayPoints) {
    SteerableComponent sourceSteering = npc.getComponent(SteerableComponent.class);
    LinePath<Vector2> linePath = new LinePath<>(wayPoints, false);
    FollowPath followPathSB = new FollowPath(sourceSteering, linePath, 1);
    followPathSB.setTimeToTarget(0.1f);
    followPathSB.setArrivalTolerance(0.01f);
    followPathSB.setDecelerationRadius(20);

    Box2dRadiusProximity proximity = new Box2dRadiusProximity(sourceSteering, world, sourceSteering.getBoundingRadius() * MPP);
    CollisionAvoidance<Vector2> collisionAvoidanceSB = new CollisionAvoidance<Vector2>(sourceSteering, proximity);

    LookWhereYouAreGoing lookWhereYouAreGoingSB = new LookWhereYouAreGoing<Vector2>(sourceSteering);
    lookWhereYouAreGoingSB.setLimiter(new AngularLimiter(sourceSteering.getMaxAngularAcceleration(), sourceSteering.getMaxAngularSpeed()));
    lookWhereYouAreGoingSB.setTimeToTarget(0.1f);
    lookWhereYouAreGoingSB.setAlignTolerance(0.001f);
    lookWhereYouAreGoingSB.setDecelerationRadius(MathUtils.PI);

    BlendedSteering<Vector2> reachPositionAndOrientationSB = new BlendedSteering<Vector2>(sourceSteering);
    reachPositionAndOrientationSB.setLimiter(NullLimiter.NEUTRAL_LIMITER);
    reachPositionAndOrientationSB.add(followPathSB, 1f);
    reachPositionAndOrientationSB.add(collisionAvoidanceSB, 1f);
    reachPositionAndOrientationSB.add(lookWhereYouAreGoingSB, 0.2f);

    sourceSteering.setBehavior(reachPositionAndOrientationSB);
  }

  public static Wander<Vector2> setWanderSteering(NPC npc) {
    SteerableComponent sourceSteering = npc.getComponent(SteerableComponent.class);
    npc.steerableComponent.setIndependetFacing(false);

    Wander<Vector2> wanderSB = new Wander<>(sourceSteering);
    wanderSB.setLimiter(new LinearAccelerationLimiter(sourceSteering.getMaxLinearAcceleration()));
    wanderSB.setFaceEnabled(false); // We want to use Face internally (independent facing is on)
    wanderSB.setAlignTolerance(0.001f); // Used by Face
    wanderSB.setDecelerationRadius(1); // Used by Face
    wanderSB.setTimeToTarget(0.1f); // Used by Face
    wanderSB.setWanderOffset(3);
    wanderSB.setWanderOrientation(3);
    wanderSB.setWanderRadius(1);
    wanderSB.setWanderRate(MathUtils.PI2 * 4);

    sourceSteering.setBehavior(wanderSB);

    return wanderSB;
  }

  public static void setBattleSteering(NPC npc) {
    npc.steerableComponent.setBehavior(null);
  }
}
