package com.nima.actors;

import box2dLight.RayHandler;

/**
 * Controls the ambient light
 */
public class AmbientLight implements Updateable {

  private RayHandler rayHandler;

  public AmbientLight(RayHandler rayHandler) {
    this.rayHandler = rayHandler;
    rayHandler.setAmbientLight(.7f, .7f, .7f, 0.7f);
  }

  @Override
  public void update() {

  }
}
