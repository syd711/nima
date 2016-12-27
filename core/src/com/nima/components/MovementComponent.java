package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.nima.actors.Location;
import com.nima.actors.Spine;
import com.nima.util.GraphicsUtil;
import com.nima.util.Settings;

/**
 * Component responsible for handling the collision of entities
 */
public class MovementComponent implements Component {

  private RotationComponent rotationComponent;
  private PositionComponent position;
  private SpineComponent spineComponent;
  private SpeedComponent speed;
  private Spine spine;

  private float mapTargetX;
  private float mapTargetY;
  private Entity target;

  public MovementComponent(Spine spine) {
    this.spine = spine;
    this.rotationComponent = spine.getComponent(RotationComponent.class);
    this.spineComponent = spine.getComponent(SpineComponent.class);
    this.position = spine.getComponent(PositionComponent.class);
    this.speed = spine.getComponent(SpeedComponent.class);
  }

  public void move() {
    if(spine != null) {
      float currentAngle = rotationComponent.getRotation();
      Vector2 delta = GraphicsUtil.getUpdatedCoordinates(currentAngle, speed.currentSpeed);
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


  public void stop() {
    speed.setTargetSpeedPercentage(0);
  }


  /**
   * Moves to the given screen coordinates
   *
   * @param x screen x
   * @param y screen y
   */
  public void moveTo(float x, float y) {
    target = null;
    this.mapTargetX = x;
    this.mapTargetY = y;
    updateRotation();
  }

  /**
   * Returns true if the target entity is a valid target to move to
   *
   * @param entity the entity that has been clicked on
   */
  public boolean moveToEntity(Entity entity) {
    if(entity instanceof Location) {
      MapObjectComponent mapObjectComponent = entity.getComponent(MapObjectComponent.class);
      Vector2 centeredPosition = mapObjectComponent.getCenteredPosition();
      moveTo(centeredPosition.x, centeredPosition.y);
      target = entity;
      return true;
    }
    return false;
  }

  public Entity getTarget() {
    return target;
  }

  // -------------------- Helper --------------------------------------

  /**
   * Calculate to turn right or left
   */
  public void updateRotation() {
    float angle = GraphicsUtil.getAngle(position.x, position.y, mapTargetX, mapTargetY) * -1;

    float modulo = Math.round(angle * -1) % Settings.ACTOR_ROTATION_SPEED;
    float targetAngle = Math.round((angle - modulo) * -1);
    float currentAngle = rotationComponent.getRotation();

    float normalizedTarget = targetAngle;
    if(normalizedTarget < 0) {
      normalizedTarget = 360 - (normalizedTarget * -1);
    }

    float normalizedSource = currentAngle;
    if(normalizedSource < 0) {
      normalizedSource = 360 - (normalizedSource * -1);
    }

    boolean rotateLeft = (normalizedTarget - normalizedSource + 360) % 360 > 180;
    rotationComponent.rotate(targetAngle, rotateLeft);
  }
}
