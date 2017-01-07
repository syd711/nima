package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.nima.actors.Location;
import com.nima.actors.Spine;
import com.nima.util.GraphicsUtil;

/**
 * Component responsible for handling the collision of entities
 */
public class MovementComponent implements Component, Pool.Poolable {

  private RotationComponent rotation;
  private PositionComponent position;
  private SpeedComponent speed;
  private BodyComponent bodyComponent;

  public void setSpine(Spine spine) {
    this.rotation = spine.getComponent(RotationComponent.class);
    this.position = spine.getComponent(PositionComponent.class);
    this.speed = spine.getComponent(SpeedComponent.class);
    this.bodyComponent = spine.getComponent(BodyComponent.class);
  }


  @Override
  public void reset() {
    this.rotation = null;
    this.position = null;
    this.speed = null;
  }

  public void move() {
    float currentAngle = rotation.getRotation();
    Vector2 delta = GraphicsUtil.getUpdatedCoordinates(currentAngle, speed.getCurrentValue());
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

//    bodyComponent.setWorldPosition(position.getPosition());
  }

  public void stop() {
    if(speed.getTargetValue() > 0) {
      speed.setTargetPercentage(0);
    }
  }

  /**
   * Moves to the given screen coordinates
   *
   * @param x screen x
   * @param y screen y
   */
  public void moveTo(float x, float y) {
    rotation.setRotationTarget(x, y);
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
      return true;
    }
    return false;
  }
}
