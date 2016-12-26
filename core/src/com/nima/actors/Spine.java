package com.nima.actors;

import com.badlogic.ashley.core.Entity;
import com.nima.components.*;
import com.nima.util.Settings;

/**
 * Superclass for spine entities
 */
abstract public class Spine extends Entity {
  protected SpineComponent spineComponent;
  protected MovementComponent movementComponent;
  protected DimensionComponent dimensionComponent;
  protected PositionComponent positionComponent;
  protected SpeedComponent speedComponent;
  protected CollisionComponent collisionComponent;

  public Spine(String path, String defaultAnimation, float scaling) {
    spineComponent = new SpineComponent(path, defaultAnimation, scaling);
    add(spineComponent);

    positionComponent = new PositionComponent(0, 0);
    add(positionComponent);

    dimensionComponent = new DimensionComponent(spineComponent);
    add(dimensionComponent);

    speedComponent = new SpeedComponent(Settings.MAX_ACTOR_SPEED);
    add(speedComponent);

    collisionComponent = new CollisionComponent(spineComponent);
    add(collisionComponent);

    movementComponent = new MovementComponent(this);
    add(movementComponent);

    //box2d
//    BodyDef def = new BodyDef();
//    def.type = BodyDef.BodyType.DynamicBody;
//    def.fixedRotation = false;
//    def.position.set(targetX, targetY);
//    body = world.createBody(def);
//
//    PolygonShape shape = new PolygonShape();
//    //calculated from center!
//    shape.setAsBox(dimensionComponent.width / 2 / Settings.PPM, dimensionComponent.height / 2 / Settings.PPM);
//    body.createFixture(shape, 1f);
//    shape.dispose();
  }
}
