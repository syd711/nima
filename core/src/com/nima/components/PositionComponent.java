package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.nima.managers.EntityProperties;

/**
 *
 */
public class PositionComponent implements Component {
  public float x = 0.0f;
  public float y = 0.0f;

  private Vector2 center = new Vector2();

  public PositionComponent() {
  }

  public PositionComponent(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public PositionComponent(MapObject object) {
    Vector2 position = (Vector2) object.getProperties().get(EntityProperties.PROPERTY_POSITION);
    this.x = position.x;
    this.y = position.y;
  }

  public void translate(float x, float y) {
    this.x += x;
    this.y += y;
  }
}
