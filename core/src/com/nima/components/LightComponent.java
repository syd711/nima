package com.nima.components;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.PositionalLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.nima.util.Settings;

/**
 * Component implementation for map objects
 */
public class LightComponent extends DelimitingComponent {
  private final PositionalLight pointLight;
  private final int MAX_RAYS = 2000;
  private boolean moveable;

  public LightComponent(RayHandler rayHandler, float distance, float x, float y, boolean moveable) {
    super(1, 1, 1, Settings.FADE_IN_OFFSET, Settings.FADE_OUT_OFFSET);
    pointLight = new PointLight(rayHandler, MAX_RAYS);
    pointLight.setDistance(distance);
    pointLight.setColor(getTargetValue(), getTargetValue(), getTargetValue(), getTargetValue());
    pointLight.setPosition(x, y);

    this.moveable = moveable;
  }

  public LightComponent(RayHandler rayHandler, float distance, float x, float y, float degree, float coneDegree, boolean moveable) {
    super(1, 1, 1, Settings.FADE_IN_OFFSET, Settings.FADE_OUT_OFFSET);
    pointLight = new ConeLight(rayHandler, MAX_RAYS, Color.WHITE, distance, x, y, degree, coneDegree);
    pointLight.setColor(getTargetValue(), getTargetValue(), getTargetValue(), getTargetValue());
    this.moveable = moveable;
  }

  public void setPosition(float x, float y) {
    pointLight.setPosition(x, y);
  }

  public boolean isMoveable() {
    return moveable;
  }
}
