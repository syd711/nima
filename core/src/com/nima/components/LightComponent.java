package com.nima.components;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.PositionalLight;
import box2dLight.RayHandler;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;

/**
 * Component implementation for map objects
 */
public class LightComponent implements Component {
  private final PositionalLight pointLight;
  private final int MAX_RAYS = 2000;
  private boolean moveable;

  public LightComponent(RayHandler rayHandler, float distance, float x, float y, boolean moveable) {
    pointLight = new PointLight(rayHandler, MAX_RAYS);
    pointLight.setDistance(distance);
    pointLight.setColor(Color.WHITE);
    pointLight.setPosition(x, y);

    this.moveable = moveable;
  }

  public LightComponent(RayHandler rayHandler, float distance, float x, float y, float degree, float coneDegree, boolean moveable) {
    pointLight = new ConeLight(rayHandler, MAX_RAYS, Color.WHITE, distance, x, y, degree, coneDegree);

    this.moveable = moveable;
  }

  public void setPosition(float x, float y) {
    pointLight.setPosition(x, y);
  }

  public boolean isMoveable() {
    return moveable;
  }
}
