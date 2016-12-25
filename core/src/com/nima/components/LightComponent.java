package com.nima.components;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;

/**
 * Component implementation for map objects
 */
public class LightComponent implements Component {
  private final PointLight pointLight;
  private final int MAX_RAYS = 2000;

  public LightComponent(RayHandler rayHandler) {
    pointLight = new PointLight(rayHandler, MAX_RAYS);
    pointLight.setDistance(4000);
    pointLight.setColor(Color.WHITE);
  }

  public void setLightPosition(float x, float y) {
    pointLight.setPosition(x, y);
  }

}
