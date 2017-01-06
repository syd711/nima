package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;

import static com.nima.util.Settings.PPM;

/**
 * Box2d support for entities
 */
public class BodyComponent implements Component, Pool.Poolable {

  public Body body;

  @Override
  public void reset() {
    this.body = null;
  }

  public Vector2 getWorldPosition() {
    Vector2 position = body.getPosition();
    return new Vector2(position.x*PPM, position.y*PPM);
  }
}
