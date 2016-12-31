package com.nima.render;

import com.badlogic.gdx.physics.box2d.Shape;

/**
 *
 */
public class LevelGeometry /*implements Collidable*/ {
  private Shape shape;

  public LevelGeometry(Shape shape) {
    this.shape = shape;
  }

  public Shape getShape() {
    return shape;
  }
}