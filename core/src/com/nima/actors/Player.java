package com.nima.actors;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.nima.components.CollisionComponent;
import com.nima.components.DimensionComponent;
import com.nima.components.PositionComponent;
import com.nima.components.SpineComponent;
import com.nima.util.Resources;
import com.nima.util.Settings;

/**
 * The player with all ashley components.
 */
public class Player {
  private DimensionComponent dimension;
  private PositionComponent position;
  private SpineComponent spine;
  private CollisionComponent collision;
  private Entity entity;

  public Player() {
    entity = new Entity();
    spine = new SpineComponent(Resources.ACTOR_SPINE, Resources.ACTOR_DEFAULT_ANIMATION, 0.3f);
    dimension = new DimensionComponent(spine);
    entity.add(spine);
    entity.add(dimension);

    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    float x = Settings.START_FRAME_X * Settings.FRAME_PIXELS_X + (w / 2);
    float y = Settings.START_FRAME_Y * Settings.FRAME_PIXELS_Y + (h / 2);

    position = new PositionComponent(x, y);
    position.x = x;
    position.y = y;
    entity.add(position);

    collision = new CollisionComponent(spine);
    entity.add(collision);
  }

  /**
   * Executes the moving to the given coordinates
   * @param screenX the click point X
   * @param screenY the click point Y
   */
  public void moveTo(float screenX, float screenY) {
    double angle = Math.atan2(screenY - position.y+dimension.height/2, screenX - position.x+dimension.width/2) * 180 / Math.PI;

    float modulo = Math.round(angle * -1) % Settings.ACTOR_ROTATION_SPEED;
    float targetAngle = Math.round((angle - modulo) * -1);
    float currentAngle = spine.getRotation();

    float normalizedTarget = targetAngle;
    if(normalizedTarget < 0) {
      normalizedTarget = 360 - (normalizedTarget * -1);
    }

    float normalizedSource = currentAngle;
    if(normalizedSource < 0) {
      normalizedSource = 360 - (normalizedSource * -1);
    }

    boolean rotateLeft = (normalizedTarget - normalizedSource + 360) % 360 > 180;
    spine.rotate(targetAngle, rotateLeft);
  }

  public void setDimensionComponent(DimensionComponent dimensionComponent) {
    this.dimension = dimensionComponent;
  }

  public PositionComponent getPositionComponent() {
    return position;
  }

  public void setPositionComponent(PositionComponent positionComponent) {
    this.position = positionComponent;
  }

  public void setSpineComponent(SpineComponent spineComponent) {
    this.spine = spineComponent;
  }

  public void setCollisionComponent(CollisionComponent collisionComponent) {
    this.collision = collisionComponent;
  }

  public CollisionComponent getCollisionComponent() {
    return collision;
  }

  public float getX() {
    return position.x;
  }

  public float getY() {
    return position.y;
  }

  public Entity getEntity() {
    return entity;
  }
}
