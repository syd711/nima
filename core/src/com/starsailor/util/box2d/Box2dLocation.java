package com.starsailor.util.box2d;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.util.GraphicsUtil;

/**
 * Used for steering
 */
public class Box2dLocation implements Location<Vector2> {
  private Vector2 position;
  private float orientation;

  public Box2dLocation (Vector2 pos) {
    this.position = pos;
    this.orientation = 0;
  }

  @Override
  public Vector2 getPosition () {
    return position;
  }

  @Override
  public float getOrientation () {
    return orientation;
  }

  @Override
  public void setOrientation (float orientation) {
    this.orientation = orientation;
  }

  @Override
  public Location<Vector2> newLocation () {
    return new Box2dLocation(new Vector2());
  }

  @Override
  public float vectorToAngle (Vector2 vector) {
    return GraphicsUtil.vectorToAngle(vector);
  }

  @Override
  public Vector2 angleToVector (Vector2 outVector, float angle) {
    return GraphicsUtil.angleToVector(outVector, angle);
  }

}