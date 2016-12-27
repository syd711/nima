package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.nima.actors.Spine;
import com.nima.util.GraphicsUtil;
import com.nima.util.Settings;

/**
 * Component for rotation
 */
public class RotationComponent implements Component {
  private float targetAngle = 0;
  private boolean rotateLeft = false;
  private Spine spine;

  public RotationComponent(Spine spine) {
    this.spine = spine;
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
  public void setRotationTarget(float x, float y, float mapTargetX, float mapTargetY) {
    float angle = GraphicsUtil.getAngle(x, y, mapTargetX, mapTargetY) * -1;

    float modulo = Math.round(angle * -1) % Settings.ACTOR_ROTATION_SPEED;
    float targetAngle = Math.round((angle - modulo) * -1);
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
    float currentAngle = spine.skeleton.getRootBone().getRootRotation();
    if(isRotating()) {
      if(rotateLeft) {
        float newAngle = currentAngle - Settings.ACTOR_ROTATION_SPEED;
        if(newAngle < -180) {
          newAngle = 180 - (newAngle % 180);
        }
        spine.skeleton.getRootBone().setRootRotation(newAngle);
      }
      else {
        float newAngle = currentAngle + Settings.ACTOR_ROTATION_SPEED;
        if(newAngle > 180) {
          newAngle = -180 + (newAngle % 180);
        }
        spine.skeleton.getRootBone().setRootRotation(newAngle);
      }
    }
  }

  public float getRotation() {
    return spine.skeleton.getRootBone().getRootRotation();
  }

}