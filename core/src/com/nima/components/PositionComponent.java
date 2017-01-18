package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.nima.actors.Player;
import com.nima.util.Box2dUtil;

/**
 *
 */
public class PositionComponent implements Component, Pool.Poolable {
  public float x = 0.0f;
  public float y = 0.0f;
  public int z = 0;

  public void setPosition(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public float distanceToPlayer() {
    return getPosition().dst(Player.getInstance().getCenter());
  }

  public void setPosition(Vector2 position) {
    this.x = position.x;
    this.y = position.y;
  }

  public Vector2 getPosition() {
    return new Vector2(x, y);
  }

  public Vector2 getBox2dPosition() {
    return Box2dUtil.toBox2Vector(getPosition());
  }

  @Override
  public void reset() {
    this.x = 0f;
    this.y = 0f;
    this.z = 0;
  }
}
