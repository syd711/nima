package com.nima.ai;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.nima.actors.Spine;
import com.nima.util.GraphicsUtil;

/**
 * The AI steering implementation for Spines.
 */
public class SpineSteerable implements Steerable<Vector2> {

  private Spine spine;
  private boolean tagged;
  private float boundingRadius;
  private float maxLinearSpeed;
  private float maxLinearAcceleration;
  private float maxAngularSpeed;
  private float maxAngularAcceleration;

  private SteeringBehavior<Vector2> behavior;
  private SteeringAcceleration<Vector2> steeringOutput;

  public SpineSteerable(Spine spine, float boundingRadius) {
    this.spine = spine;
    this.boundingRadius = boundingRadius;
  }

  public void update(float delta) {

  }

  @Override
  public Vector2 getLinearVelocity() {
    return new Vector2(5, 5);
  }

  @Override
  public float getAngularVelocity() {
    return 1 ;
  }

  @Override
  public float getBoundingRadius() {
    return boundingRadius;
  }

  @Override
  public boolean isTagged() {
    return tagged;
  }

  @Override
  public void setTagged(boolean tagged) {
    this.tagged = tagged;
  }

  @Override
  public float getZeroLinearSpeedThreshold() {
    return 0;
  }

  @Override
  public void setZeroLinearSpeedThreshold(float value) {

  }

  @Override
  public float getMaxLinearSpeed() {
    return maxLinearSpeed;
  }

  @Override
  public void setMaxLinearSpeed(float maxLinearSpeed) {
    this.maxLinearSpeed = maxLinearSpeed;
  }

  @Override
  public float getMaxLinearAcceleration() {
    return maxLinearAcceleration;
  }

  @Override
  public void setMaxLinearAcceleration(float maxLinearAcceleration) {
    this.maxLinearAcceleration = maxLinearAcceleration;
  }

  @Override
  public float getMaxAngularSpeed() {
    return maxAngularSpeed;
  }

  @Override
  public void setMaxAngularSpeed(float maxAngularSpeed) {
    this.maxAngularSpeed = maxAngularSpeed;
  }

  @Override
  public float getMaxAngularAcceleration() {
    return maxAngularAcceleration;
  }

  @Override
  public void setMaxAngularAcceleration(float maxAngularAcceleration) {
    this.maxAngularAcceleration = maxAngularAcceleration;
  }

  @Override
  public Vector2 getPosition() {
    return spine.getCenter();
  }

  @Override
  public float getOrientation() {
    return spine.skeleton.getRootBone().getRootRotation();
  }

  @Override
  public void setOrientation(float orientation) {
    spine.skeleton.getRootBone().setRootRotation(orientation);
  }

  @Override
  public float vectorToAngle(Vector2 vector) {
    return GraphicsUtil.vectorToAngle(vector);
  }

  @Override
  public Vector2 angleToVector(Vector2 outVector, float angle) {
    return GraphicsUtil.angleToVector(outVector, angle);
  }

  @Override
  public Location<Vector2> newLocation() {
    return null;
  }

  public SteeringBehavior<Vector2> getBehavior() {
    return behavior;
  }

  public void setBehavior(SteeringBehavior<Vector2> behavior) {
    this.behavior = behavior;
  }

  public void setSteeringOutput(SteeringAcceleration<Vector2> steeringOutput) {
    this.steeringOutput = steeringOutput;
  }

  public SteeringAcceleration<Vector2> getSteeringOutput() {
    return steeringOutput;
  }
}
