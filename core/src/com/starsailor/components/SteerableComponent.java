package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;
import com.starsailor.data.SteeringData;
import com.starsailor.systems.behaviours.FaceBehaviour;
import com.starsailor.util.GraphicsUtil;
import com.starsailor.util.Settings;

/**
 * AI steerable implementation for box2d bodies.
 */
public class SteerableComponent implements Component, Steerable<Vector2>, Pool.Poolable {
  private boolean tagged = false;
  private float boundingRadius;
  private float maxLinearSpeed;
  private float maxLinearAcceleration;
  private float maxAngularSpeed;
  private float maxAngularAcceleration;

  public boolean enabled = true;

  private SteeringBehavior<Vector2> behavior;
  private FaceBehaviour faceBehaviour;
  private SteeringAcceleration<Vector2> steeringOutput;

  private Body body;

  public void init(Body body, SteeringData steeringData) {
    this.body = body;

    this.boundingRadius = steeringData.boundingRadius;
    this.maxLinearSpeed = steeringData.maxLinearSpeed;
    this.maxLinearAcceleration = steeringData.maxLinearAcceleration;
    this.maxAngularSpeed = steeringData.maxAngularSpeed;
    this.maxAngularAcceleration = steeringData.maxAngularAcceleration;

    this.steeringOutput = new SteeringAcceleration<>(new Vector2());
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public void reset() {
    this.body = null;
    this.boundingRadius = -1;

    this.maxLinearSpeed = -1;
    this.maxLinearAcceleration = -1;
    this.maxAngularSpeed = -1;
    this.maxAngularAcceleration = -1;
    this.tagged = false;

    this.steeringOutput = new SteeringAcceleration<>(new Vector2());
  }


  public void update(float delta) {
    if(Settings.getInstance().steering_enabled && isEnabled()) {
      if(behavior != null) {
        behavior.calculateSteering(steeringOutput);
        applySteering(delta);
      }

      if(faceBehaviour != null) {
        faceBehaviour.update();
      }
    }
  }

  private void applySteering(float delta) {
    boolean anyAccelerations = false;
    if(!steeringOutput.isZero()) {
      Vector2 force = steeringOutput.linear.scl(delta);
      body.applyForceToCenter(force, true);
      anyAccelerations = true;
    }

//    if(steeringOutput.angular != 0) {
//      body.applyTorque(steeringOutput.angular * delta, true);
//      anyAccelerations = true;
//    }
//    else {
//      Vector2 linVel = getLinearVelocity();
//      if(!linVel.isZero()) {
//        float newOrientation = vectorToAngle(linVel);
//        body.setAngularVelocity((newOrientation - getAngularVelocity())*delta);
//        body.setTransform(body.getPosition(), newOrientation);
//      }
//    }



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

  public void setFaceBehaviour(FaceBehaviour faceBehaviour) {
    this.faceBehaviour = faceBehaviour;
  }

  public FaceBehaviour getFaceBehaviour() {
    return faceBehaviour;
  }
}
