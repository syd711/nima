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

  private float mapTargetX = -1f;
  private float mapTargetY = -1f;

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
  public void setRotationTarget(float mapTargetX, float mapTargetY) {
    this.mapTargetX = mapTargetX;
    this.mapTargetY = mapTargetY;

    float targetAngle = getTargetAngle();
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

  public float getB2dAngle() {
    BodyComponent bodyComponent = spine.getComponent(BodyComponent.class);
    double DEGREES_TO_RADIANS = Math.PI/180;
    float angle = spine.skeleton.getRootBone().getRootRotation();
    float b2dAngle = (float) (angle*DEGREES_TO_RADIANS);
    System.out.println(b2dAngle);
    return b2dAngle;
  }

  public float getRotation() {
    return spine.skeleton.getRootBone().getRootRotation();
  }

  private float getTargetAngle() {
    float x = spine.getCenter().x;
    float y = spine.getCenter().y;
    float angle = GraphicsUtil.getAngle(x, y, mapTargetX, mapTargetY) * -1;
    float modulo = Math.round(angle * -1) % Settings.ACTOR_ROTATION_SPEED;
    float targetAngle = (float) Math.round((angle - modulo) * -1);
    //avoid spinning
    if(targetAngle == -180) {
      targetAngle = -178;
    }
//    System.out.println("Target Angle: " + targetAngle);
    return targetAngle;
  }

}
