package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.nima.actors.Location;
import com.nima.actors.Spine;
import com.nima.util.GraphicsUtil;
import com.nima.util.Settings;

/**
 * Component responsible for handling the collision of entities
 */
public class MovementComponent implements Component {

  private CollisionComponent collisionComponent;
  private PositionComponent position;
  private SpineComponent spineComponent;
  private SpeedComponent speed;
  private Spine spine;

  private float mapTargetX;
  private float mapTargetY;

  private boolean move = false;

  public MovementComponent(Spine spine) {
    this.spine = spine;
    this.collisionComponent = spine.getComponent(CollisionComponent.class);
    this.spineComponent = spine.getComponent(SpineComponent.class);
    this.position = spine.getComponent(PositionComponent.class);
    this.speed = spine.getComponent(SpeedComponent.class);
  }

  public void move() {
    if(spine != null) {
      if(move) {
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
    move = true;
    this.mapTargetX = x;
    this.mapTargetY = y;
    moveSpine();
  }

  /**
   * Returns true if the target entity is a valid target to move to
   * @param entity the entity that has been clicked on
   */
  public boolean moveToEntity(Entity entity) {
    if(entity instanceof Location) {

      return true;
    }
    return false;
  }


  // -------------------- Helper --------------------------------------

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
    Vector2 point1 = new Vector2(position.x, position.y);
    Vector2 point2 = new Vector2(mapTargetX, mapTargetY);

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
//    float angle = GraphicsUtil.getAngle(screenPosition.getX(), screenPosition.getY(), screenTargetX, screenTargetY) * -1;
    float angle = GraphicsUtil.getAngle(position.x, position.y, mapTargetX, mapTargetY) * -1;

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

  public boolean isMove() {
    return move;
  }

  public void setMove(boolean move) {
    this.move = move;
  }
}
