package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.nima.Main;
import com.nima.actors.Spine;
import com.nima.util.GraphicsUtil;
import com.nima.util.Settings;

/**
 * Component responsible for handling the collision of entities
 */
public class MovementComponent implements Component {

  private ScreenPositionComponent screenPosition;
  private CollisionComponent collisionComponent;
  private PositionComponent position;
  private SpineComponent spineComponent;
  private SpeedComponent speed;
  private Spine spine;

  private float targetX;
  private float targetY;

  public MovementComponent(Spine spine) {
    this.spine = spine;
    this.screenPosition = spine.getComponent(ScreenPositionComponent.class);
    this.collisionComponent = spine.getComponent(CollisionComponent.class);
    this.spineComponent = spine.getComponent(SpineComponent.class);
    this.position = spine.getComponent(PositionComponent.class);
    this.speed = spine.getComponent(SpeedComponent.class);

    this.targetX = this.position.x;
    this.targetY = this.position.y;
  }

  public void move() {
    if(spine != null) {
      if(position.x != targetX || position.y != targetY) {
        if(spineComponent.isRotating() && speed.isAtFullSpeed()) {
          speed.setCurrentValue(speed.currentValue - speed.targetValue * 30 / 100);
        }
        else if(!speed.isAtFullSpeed()) {
          speed.setCurrentValue(speed.currentValue + speed.targetValue * 1 / 100);
        }

        float currentAngle = spineComponent.getRotation();
        Vector2 delta = GraphicsUtil.getUpdatedCoordinates(currentAngle, speed.currentValue);
        float x = delta.x;
        float y = delta.y;

        if(currentAngle >= 0 && currentAngle <= 90) {
          position.x = position.x + x;
          position.y = position.y + y;
        }
        else if(currentAngle > 90 && currentAngle <= 180) {
          position.x = position.x - x;
          position.y = position.y + y;
        }
        else if(currentAngle < 0 && currentAngle >= -90) {
          position.x = position.x + x;
          position.y = position.y - y;
        }
        else if(currentAngle < -90 && currentAngle >= -180) {
          position.x = position.x - x;
          position.y = position.y - y;
        }
      }
    }

    updateBody();
  }

  private void updateBody() {
    collisionComponent.updateBody();
  }

  /**
   * Moves to the given screen coordinates
   * @param x  screen x
   * @param y  screen y
   */
  public void moveTo(float x, float y) {
    this.targetX = x;
    this.targetY = y;
    moveSpine();
  }

  private void moveSpine() {
    if(updateSpeed() > 0) {
      updateRotation();
    }
  }

  /**
   * Updates the spine speed depending
   * on the click distance
   */
  private float updateSpeed() {
    Vector2 point1 = new Vector2(screenPosition.getX(), screenPosition.getY());
    Vector2 point2 = new Vector2(targetX, targetY);

    //TODO offset not working
    float speedOffset = 30; //px
    float distance = point1.dst(point2) - speedOffset;
    float percentage = 0;
    if(distance > 0) {
      float maxDistance = Gdx.graphics.getHeight()/2;
      if(distance > maxDistance) {
        distance = maxDistance;
      }

      percentage = distance * 100 / maxDistance;
    }

    if(percentage < 10) {
      percentage = 0;
    }

    speed.setTargetSpeedPercentage(percentage);
    return percentage;
  }

  /**
   * Calculate to turn right or left
   */
  private void updateRotation() {
//    float angle = GraphicsUtil.getAngle(screenPosition.getX(), screenPosition.getY(), targetX, targetY) * -1;
    Vector2 target = GraphicsUtil.transform2WorldCoordinates(Main.camera, targetX, targetY);
    float angle = GraphicsUtil.getAngle(position.x, position.y, target.x, target.y) * -1;

    float modulo = Math.round(angle * -1) % Settings.ACTOR_ROTATION_SPEED;
    float targetAngle = Math.round((angle - modulo) * -1);
    float currentAngle = spineComponent.getRotation();

    float normalizedTarget = targetAngle;
    if(normalizedTarget < 0) {
      normalizedTarget = 360 - (normalizedTarget * -1);
    }

    float normalizedSource = currentAngle;
    if(normalizedSource < 0) {
      normalizedSource = 360 - (normalizedSource * -1);
    }

    boolean rotateLeft = (normalizedTarget - normalizedSource + 360) % 360 > 180;
    spineComponent.rotate(targetAngle, rotateLeft);
  }
}
