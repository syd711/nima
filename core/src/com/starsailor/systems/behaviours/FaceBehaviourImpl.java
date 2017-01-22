package com.starsailor.systems.behaviours;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Custom FACE behaviour.
 * We pretend it to be a real SteeringBehaviour to use the update method.
 */
public class FaceBehaviourImpl implements FaceBehaviour {
  private Body behaviourOwner;
  private Body behaviourTarget;
  private float degrees;

  public FaceBehaviourImpl(Body behaviourOwner, Body behaviourTarget) {
    this(behaviourOwner, behaviourTarget, 1f);
  }

  public FaceBehaviourImpl(Body behaviourOwner, Body behaviourTarget, float degrees) {
    this.degrees = degrees;
    this.behaviourOwner = behaviourOwner;
    this.behaviourTarget = behaviourTarget;
  }

  public void update() {
    float bodyAngle = behaviourOwner.getAngle();
    Vector2 toTarget = behaviourTarget.getPosition().sub(behaviourOwner.getPosition());
    float desiredAngle = (float) Math.atan2(-toTarget.x, toTarget.y);

    float totalRotation = desiredAngle - bodyAngle;
    float change = (float) (1 * Math.toRadians(degrees)); //allow 1 degree rotation per time step
    float newAngle = bodyAngle + Math.min(change, Math.max(-change, totalRotation));

    behaviourOwner.setTransform(behaviourOwner.getPosition(), newAngle);
  }
}
