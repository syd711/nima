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
public class LightComponent extends LimitingComponent {
  private PositionalLight pointLight;
  private final int MAX_RAYS = 2000;
  private boolean moveable;

  public void init(RayHandler rayHandler, float distance, float x, float y, boolean moveable) {
    super.init(1, 1, 1, Settings.FADE_IN_OFFSET+0.01f, Settings.FADE_OUT_OFFSET+0.01f);
    pointLight = new PointLight(rayHandler, MAX_RAYS);
    pointLight.setDistance(distance);
    pointLight.setColor(getTargetValue(), getTargetValue(), getTargetValue(), getTargetValue());
    pointLight.setPosition(x, y);

    this.moveable = moveable;
  }

  public void init(RayHandler rayHandler, float distance, float x, float y, float degree, float coneDegree, boolean moveable) {
    super.init(1, 1, 1, Settings.FADE_IN_OFFSET+0.01f, Settings.FADE_OUT_OFFSET+0.01f);
    pointLight = new ConeLight(rayHandler, MAX_RAYS, Color.WHITE, distance, x, y, degree, coneDegree);
    pointLight.setColor(getTargetValue(), getTargetValue(), getTargetValue(), getTargetValue());
    this.moveable = moveable;
  }

  @Override
  public void updateValue() {
    super.updateValue();
    if(getCurrentValue() != getTargetValue()) {
      pointLight.setColor(getCurrentValue(), getCurrentValue(), getCurrentValue(), getCurrentValue());
    }
  }

  public void setPosition(float x, float y) {
    pointLight.setPosition(x, y);
  }

  public boolean isMoveable() {
    return moveable;
  }


  @Override
  public void reset() {
    super.reset();
    this.pointLight = null;
  }
}
