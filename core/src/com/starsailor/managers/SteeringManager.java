package com.starsailor.managers;

import com.badlogic.gdx.ai.steer.behaviors.*;
import com.badlogic.gdx.ai.steer.limiters.AngularLimiter;
import com.badlogic.gdx.ai.steer.limiters.LinearAccelerationLimiter;
import com.badlogic.gdx.ai.steer.limiters.NullLimiter;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Ship;
import com.starsailor.components.SteerableComponent;
import com.starsailor.util.box2d.Box2dRadiusProximity;

import static com.starsailor.Game.world;
import static com.starsailor.util.Settings.MPP;

/**
 * Used to apply steering behaviours to entities
 */
public class SteeringManager {

  public static void setGuardSteering(NPC npc) {
    SteerableComponent sourceSteering = npc.getComponent(SteerableComponent.class);

    Arrive<Vector2> arrive = new Arrive<>(sourceSteering, npc.getTargetLocation());
    arrive.setTimeToTarget(0.1f);
    arrive.setArrivalTolerance(0.2f);
    arrive.setDecelerationRadius(10);

    Box2dRadiusProximity proximity = new Box2dRadiusProximity(sourceSteering, world, sourceSteering.getBoundingRadius() * MPP);
    CollisionAvoidance<Vector2> collisionAvoidanceSB = new CollisionAvoidance<Vector2>(sourceSteering, proximity);

    LookWhereYouAreGoing lookWhereYouAreGoingSB = new LookWhereYouAreGoing<>(sourceSteering);
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
    FollowPath followPathSB = new FollowPath<>(sourceSteering, linePath, 1);
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

    BlendedSteering<Vector2> blendedSteering = new BlendedSteering<Vector2>(sourceSteering);
    blendedSteering.setLimiter(NullLimiter.NEUTRAL_LIMITER);
    blendedSteering.add(followPathSB, 1f);
    blendedSteering.add(collisionAvoidanceSB, 1f);
    blendedSteering.add(lookWhereYouAreGoingSB, 1f);

    sourceSteering.setBehavior(blendedSteering);
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

  public static void setFaceSteering(SteerableComponent sourceSteering, SteerableComponent targetSteering, float boundingRadius) {
    Box2dRadiusProximity proximity = new Box2dRadiusProximity(sourceSteering, world, boundingRadius * MPP);
    CollisionAvoidance<Vector2> collisionAvoidanceSB = new CollisionAvoidance<Vector2>(sourceSteering, proximity);

    final Face<Vector2> faceSB = new Face<>(sourceSteering, targetSteering);
    faceSB.setTimeToTarget(0.01f);
    faceSB.setAlignTolerance(0.0001f);
    faceSB.setDecelerationRadius(MathUtils.degreesToRadians * 120);

    BlendedSteering<Vector2> blendedSteering = new BlendedSteering<>(sourceSteering);
    blendedSteering.setLimiter(NullLimiter.NEUTRAL_LIMITER);
    blendedSteering.add(faceSB, 1f);
    blendedSteering.add(collisionAvoidanceSB, 1f);

    sourceSteering.setBehavior(blendedSteering);
  }

  public static void setAttackSteering(SteerableComponent sourceSteering, SteerableComponent targetSteering) {
    //we use a smaller collision avoidance here!
    Box2dRadiusProximity proximity = new Box2dRadiusProximity(sourceSteering, world, sourceSteering.getBoundingRadius()/2 * MPP);
    CollisionAvoidance<Vector2> collisionAvoidanceSB = new CollisionAvoidance<Vector2>(sourceSteering, proximity);

    final Face<Vector2> faceSB = new Face<>(sourceSteering, targetSteering);
    faceSB.setTimeToTarget(0.01f);
    faceSB.setAlignTolerance(0.0001f);
    faceSB.setDecelerationRadius(MathUtils.degreesToRadians * 120);

    Arrive<Vector2> arriveSB = new Arrive<>(sourceSteering, targetSteering);
    arriveSB.setTimeToTarget(0.1f);
    arriveSB.setArrivalTolerance(0.2f);
    arriveSB.setDecelerationRadius(10);

    BlendedSteering<Vector2> blendedSteering = new BlendedSteering<>(sourceSteering);
    blendedSteering.setLimiter(NullLimiter.NEUTRAL_LIMITER);
    blendedSteering.add(faceSB, 1f);
    blendedSteering.add(collisionAvoidanceSB, 1f);
    blendedSteering.add(arriveSB, 0.31f);

    sourceSteering.setBehavior(blendedSteering);
  }

  public static void setMissileSteering(SteerableComponent sourceSteering, SteerableComponent targetSteering) {
    final Face<Vector2> faceSB = new Face<>(sourceSteering, targetSteering);
    faceSB.setTimeToTarget(100f);
    faceSB.setAlignTolerance(0.0001f);
    faceSB.setDecelerationRadius(MathUtils.degreesToRadians * 120);

    Pursue pursueSB = new Pursue<>(sourceSteering, targetSteering);
    pursueSB.setMaxPredictionTime(0f);

    BlendedSteering<Vector2> blendedSteering = new BlendedSteering<>(sourceSteering);
    blendedSteering.setLimiter(NullLimiter.NEUTRAL_LIMITER);
    blendedSteering.add(faceSB, 0.4f);
//    blendedSteering.add(pursueSB, 1f);

    sourceSteering.setBehavior(blendedSteering);
  }

  public static void setFleeSteering(NPC npc, Ship attacker) {
    SteerableComponent sourceSteering = npc.getComponent(SteerableComponent.class);
    SteerableComponent targetSteering = attacker.getComponent(SteerableComponent.class);

    Flee<Vector2> fleeSB= new Flee<>(sourceSteering, targetSteering);
    sourceSteering.setBehavior(fleeSB);

  }

  public static void setFollowClickTargetSteering(SteerableComponent sourceSteering, SteerableComponent targetSteering) {
    Arrive<Vector2> arriveSB = new Arrive<>(sourceSteering, targetSteering);

    LookWhereYouAreGoing<Vector2> lookWhereYouAreGoingSB = new LookWhereYouAreGoing<>(sourceSteering);
    lookWhereYouAreGoingSB.setTimeToTarget(0.1f);
    lookWhereYouAreGoingSB.setAlignTolerance(0.01f);
    lookWhereYouAreGoingSB.setDecelerationRadius(MathUtils.PI);

    Face<Vector2> faceSB = new Face<>(sourceSteering, targetSteering);
    faceSB.setTimeToTarget(0.1f);
    faceSB.setAlignTolerance(0.001f);
    faceSB.setDecelerationRadius(MathUtils.degreesToRadians * 180);

    BlendedSteering<Vector2> blendedSteering = new BlendedSteering<>(sourceSteering);
    blendedSteering.add(arriveSB, 1f);
    blendedSteering.add(lookWhereYouAreGoingSB, 1f);

    sourceSteering.setBehavior(blendedSteering);
  }
}
