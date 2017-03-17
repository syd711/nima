package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Pool;
import com.starsailor.Game;
import com.starsailor.util.GraphicsUtil;
import com.starsailor.util.box2d.Box2dUtil;

import static com.starsailor.util.Settings.MPP;
import static com.starsailor.util.Settings.PPM;

/**
 * Box2d support for entities
 */
public class BodyComponent implements Component, Pool.Poolable {

  public Body body;

  public float targetRadius;

  @Override
  public void reset() {
    this.body = null;
  }

  public Vector2 getWorldPosition() {
    Vector2 position = body.getPosition();
    return new Vector2(position.x * PPM, position.y * PPM);
  }

  public void setWorldPosition(Vector2 worldPosition) {
    Vector2 pos = Box2dUtil.toBox2Vector(worldPosition);
    body.setTransform(pos.x, pos.y, body.getAngle());
  }

  public void destroy() {
    if(body != null) {
      Game.world.destroyBody(body);
      body = null;
    }
  }

  public void updateBody() {
    if(body != null && targetRadius > 0) {
      Fixture fixture = body.getFixtureList().get(0);
      Shape shape = fixture.getShape();
      if(shape instanceof CircleShape) {
        CircleShape s = (CircleShape) shape;
        float radius = GraphicsUtil.round(s.getRadius(), 2);
        if(radius == targetRadius) {
          return;
        }

        if(s.getRadius() < targetRadius) {
          s.setRadius(s.getRadius() + 0.01f);
        }
        else {
          s.setRadius(s.getRadius() - 0.01f);
        }

      }
    }
  }

  public void setTargetRadius(float f) {
    this.targetRadius = f * MPP;
  }

  public float distanceTo(Body body) {
    return body.getPosition().dst(body.getPosition());
  }
}
