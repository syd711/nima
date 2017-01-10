package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.nima.actors.Spine;
import com.nima.util.GraphicsUtil;

/**
 * Component for rotation
 */
public class RotationComponent implements Component, Pool.Poolable {
  private float targetAngle = 0;
  private boolean rotateLeft = false;
  private float mapTargetX = -1f;
  private float mapTargetY = -1f;

  public Spine spine;
  public float rotationSpeed = -1f;

  @Override
  public void reset() {
    this.spine = null;
    this.targetAngle = 0;
    this.mapTargetX = -1f;
    this.mapTargetY = -1f;
    this.rotationSpeed = -1f;
  }

  public void rotate(float angle, boolean rotateLeft) {
    this.targetAngle = angle;
    this.rotateLeft = rotateLeft;
  }

  public boolean isRotating() {
    float currentAngle = spine.skeleton.getRootBone().getRootRotation();
    return currentAngle != targetAngle;
  }

  /**
   * Calculate to turn right or left
   */
  public void setRotationTarget(float mapTargetX, float mapTargetY) {
    this.mapTargetX = mapTargetX;
    this.mapTargetY = mapTargetY;

    float targetAngle = getTargetAngle();
    setRotationTarget(targetAngle);
  }

  public void setRotationTarget(float targetAngle) {
    float currentAngle = getRotation();

    float normalizedTarget = targetAngle;
    if(normalizedTarget < 0) {
      normalizedTarget = 360 - (normalizedTarget * -1);
    }

    float normalizedSource = currentAngle;
    if(normalizedSource < 0) {
      normalizedSource = 360 - (normalizedSource * -1);
    }

    boolean rotateLeft = (normalizedTarget - normalizedSource + 360) % 360 > 180;
    rotate(targetAngle, rotateLeft);
  }

  /**
   * Checks if a rotation has been applied that is not finished yet.
   */
  public void updateRotation() {
    if(mapTargetX < 0) {
      return;
    }

    float currentAngle = spine.skeleton.getRootBone().getRootRotation();
    float updateAngle = getTargetAngle();
    if(currentAngle == updateAngle || currentAngle+2 == updateAngle || currentAngle-2 == updateAngle) {
      mapTargetX = -1;
    }
    else {
      targetAngle = updateAngle;
    }

    if(isRotating()) {
      if(rotateLeft) {
        float newAngle = currentAngle - rotationSpeed;
        if(newAngle < -180) {
          newAngle = 180 - (newAngle % 180);
        }
        spine.skeleton.getRootBone().setRootRotation(newAngle);

      }
      else {
        float newAngle = currentAngle + rotationSpeed;
        if(newAngle > 180) {
          newAngle = -180 + (newAngle % 180);
        }
        spine.skeleton.getRootBone().setRootRotation(newAngle);
      }
    }
  }

  public float getB2dAngle() {
    double DEGREES_TO_RADIANS = Math.PI/180;
    float angle = spine.skeleton.getRootBone().getRootRotation();
    float b2dAngle = (float) (angle*DEGREES_TO_RADIANS);
    return b2dAngle;
  }

  public float getRotation() {
    return spine.skeleton.getRootBone().getRootRotation();
  }

  private float getTargetAngle() {
    float x = spine.getCenter().x;
    float y = spine.getCenter().y;
    float angle = GraphicsUtil.getAngle(x, y, mapTargetX, mapTargetY) * -1;
    float modulo = Math.round(angle * -1) % rotationSpeed;
    float targetAngle = (float) Math.round((angle - modulo) * -1);
    //avoid spinning
    if(targetAngle == -180) {
      targetAngle = -178;
    }
    return targetAngle;
  }
}
