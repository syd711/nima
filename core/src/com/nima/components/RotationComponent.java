package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.nima.actors.Spine;
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
   * Checks if a rotation has been applied that is not finished yet.
   */
  public void updateRotation() {
    float currentAngle = spine.skeleton.getRootBone().getRootRotation();
    if(isRotating()) {
      if(rotateLeft) {
        float newAngle = currentAngle - Settings.ACTOR_ROTATION_SPEED;
        if(newAngle < -180) {
          newAngle = 180-(newAngle%180);
        }
        spine.skeleton.getRootBone().setRootRotation(newAngle);
      }
      else {
        float newAngle = currentAngle + Settings.ACTOR_ROTATION_SPEED;
        if(newAngle > 180) {
          newAngle = -180+(newAngle%180);
        }
        spine.skeleton.getRootBone().setRootRotation(newAngle);
      }
    }
  }

  public float getRotation() {
    return spine.skeleton.getRootBone().getRootRotation();
  }

}
