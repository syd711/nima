package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nima.util.GraphicsUtil;

/**
 * AI steerable implementation for box2d bodies.
 */
public class SteerableComponent implements Component, Steerable<Vector2> {
  private boolean tagged;
  private float boundingRadius;
  private float maxLinearSpeed;
  private float maxLinearAcceleration;
  private float maxAngularSpeed;
  private float maxAngularAcceleration;

  private SteeringBehavior<Vector2> behavior;
  private SteeringAcceleration<Vector2> steeringOutput;

  private Body body;

  public SteerableComponent(Body body) {
    this.body = body;

    this.body = body;
    this.boundingRadius = 200;

    this.maxLinearSpeed = 500;
    this.maxLinearAcceleration = 500;
    this.maxAngularSpeed = 30;
    this.maxAngularAcceleration = 10;
    this.tagged = false;

    this.steeringOutput = new SteeringAcceleration<>(new Vector2());
  }

  public void update(float delta) {
    if(behavior != null) {
      behavior.calculateSteering(steeringOutput);
      applySteering(delta);
    }
  }

  private void applySteering(float delta) {
    boolean anyAccelerations = false;
    if(!steeringOutput.isZero()) {
      Vector2 force = steeringOutput.linear.scl(delta);
      body.applyForceToCenter(force, true);
      anyAccelerations = true;
    }

    if(steeringOutput.angular != 0) {
      body.applyTorque(steeringOutput.angular * delta, true);
      anyAccelerations = true;
    }
    else {
      Vector2 linVel = getLinearVelocity();
      if(!linVel.isZero()) {
        float newOrientation = vectorToAngle(linVel);
        body.setAngularVelocity((newOrientation - getAngularVelocity())*delta);
        body.setTransform(body.getPosition(), newOrientation);
      }
    }



    if(anyAccelerations) {
      //Linear capping
      Vector2 velocity = body.getLinearVelocity();
      float currentSpeedSquare = velocity.len2();
      if(currentSpeedSquare > maxLinearSpeed * maxLinearSpeed) {
        body.setLinearVelocity(velocity.scl((float) (maxLinearSpeed / Math.sqrt(currentSpeedSquare))));
      }

      //Angular capping
      if(body.getAngularVelocity() > maxAngularSpeed) {
        body.setAngularVelocity(maxAngularSpeed);
      }
    }
  }


  @Override
  public Vector2 getLinearVelocity() {
    return body.getLinearVelocity();
  }

  @Override
  public float getAngularVelocity() {
    return body.getAngularVelocity() ;
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
    return 0.001f ;
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
    return body.getPosition();
  }

  @Override
  public float getOrientation() {
    return body.getAngle();
  }

  @Override
  public void setOrientation(float orientation) {

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
