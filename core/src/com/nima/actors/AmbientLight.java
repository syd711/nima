package com.nima.actors;

import box2dLight.RayHandler;

/**
 * Controls the ambient light
 */
public class AmbientLight implements Updateable {

  private RayHandler rayHandler;
  private float brightness = 0.7f;

  public AmbientLight(RayHandler rayHandler) {
    this.rayHandler = rayHandler;
    rayHandler.setAmbientLight(brightness, brightness,brightness,brightness);
  }

  @Override
  public void update() {
//    if(GameStateManager.getInstance().isInGameMenu()) {
      if(brightness > 0) {
        brightness = brightness-0.01f;
      }
      if(brightness < 0) {
        brightness = 0;
      }
//    }
//    else {
//      brightness = 0.7f;
//    }
    rayHandler.setAmbientLight(brightness, brightness,brightness,brightness);
  }
}
