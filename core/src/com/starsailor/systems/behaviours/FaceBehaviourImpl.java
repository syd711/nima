package com.starsailor.systems.behaviours;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import static com.starsailor.util.Settings.PPM;

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
    Vector2 toTarget = new Vector2(behaviourTarget.getPosition()).sub(behaviourOwner.getPosition());
    float distance = behaviourTarget.getPosition().dst(behaviourOwner.getPosition());
    float desiredAngle = (float) Math.atan2(-toTarget.x, toTarget.y);

    if(distance*PPM < 100) {
      return;
    }

    float totalRotation = desiredAngle - bodyAngle;
    float change = (float) (1 * Math.toRadians(degrees)); //allow 1 degree rotation per time step
    float newAngle = bodyAngle + Math.min(change, Math.max(-change, totalRotation));
    if(Math.toDegrees(change) > 180) {
      newAngle = bodyAngle - Math.min(change, Math.max(-change, totalRotation));
    }

    behaviourOwner.setTransform(behaviourOwner.getPosition(), newAngle);
  }

  @Override
  public boolean isValid() {
    return !behaviourTarget.isActive();
  }
}
