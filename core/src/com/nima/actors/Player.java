package com.nima.actors;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.nima.components.*;
import com.nima.util.MathUtil;
import com.nima.util.Resources;
import com.nima.util.Settings;

/**
 * The player with all ashley components.
 */
public class Player implements Updateable {
  private DimensionComponent dimension;
  private PositionComponent position;
  private ScreenPositionComponent screenPosition;
  private SpineComponent spine;
  private CollisionComponent collision;

  private SpeedComponent speed;
  private Entity entity;
  private float targetX;

  private float targetY;
  //box2d
  private Body body;

  public Player(World world) {
    entity = new Entity();
    spine = new SpineComponent(Resources.ACTOR_SPINE, Resources.ACTOR_DEFAULT_ANIMATION, 0.3f);
    dimension = new DimensionComponent(spine);
    entity.add(spine);
    entity.add(dimension);

    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    targetX = Settings.START_FRAME_X * Settings.FRAME_PIXELS_X + (w / 2);
    targetY = Settings.START_FRAME_Y * Settings.FRAME_PIXELS_Y + (h / 2)+dimension.height/2;
//    targetY = Settings.START_FRAME_Y * Settings.FRAME_PIXELS_Y + (h / 2);

    position = new PositionComponent(targetX, targetY);
    entity.add(position);

    screenPosition = new ScreenPositionComponent(targetX, targetY);
    entity.add(screenPosition);

    speed = new SpeedComponent(Settings.MAX_ACTOR_SPEED);
    entity.add(speed);

    collision = new CollisionComponent(spine);
    entity.add(collision);

    //box2d
    BodyDef def = new BodyDef();
    def.type = BodyDef.BodyType.DynamicBody;
    def.fixedRotation = false;
    def.position.set(targetX, targetY);
    body = world.createBody(def);

    PolygonShape shape = new PolygonShape();
    //calculated from center!
    shape.setAsBox(dimension.width / 2 / Settings.PPM, dimension.height / 2 / Settings.PPM);
    body.createFixture(shape, 1f);
    shape.dispose();
  }

  public Body getBody() {
    return body;
  }

  @Override
  public void update() {
    if(position.x != targetX || position.y != targetY) {
      if(spine.isRotating() && speed.isAtFullSpeed()) {
        speed.setCurrentValue(speed.currentValue - speed.targetValue * 40 / 100);
      }
      else if(!speed.isAtFullSpeed()) {
        speed.setCurrentValue(speed.currentValue + speed.targetValue * 1 / 100);
      }

      float currentAngle = spine.getRotation();
      Vector2 delta = MathUtil.getUpdatedCoordinates(currentAngle, speed.currentValue);
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

  /**
   * Executes the moving to the given coordinates
   *
   * @param screenX the click point X
   * @param screenY the click point Y
   */
  public void moveTo(float screenX, float screenY) {
    this.targetX = screenX;
    this.targetY = Gdx.graphics.getHeight() - screenY;
    float angle = MathUtil.getAngle(screenPosition.getX(), screenPosition.getY(), targetX, targetY) * -1;

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

  public ScreenPositionComponent getScreenPosition() {
    return screenPosition;
  }
}
