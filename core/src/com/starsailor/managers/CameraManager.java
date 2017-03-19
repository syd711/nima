package com.starsailor.managers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.starsailor.actors.Player;
import com.starsailor.components.PositionComponent;
import com.starsailor.components.ScreenPositionComponent;
import com.starsailor.util.Settings;

import java.util.Random;

public class CameraManager {
  private static final double ZOOM_DELTA = 0.05;
  private OrthographicCamera camera;
  private PositionComponent positionComponent;
  private ScreenPositionComponent screenPositionComponent;

  private static CameraManager instance = new CameraManager();

  //variables used for a shaking effect
  private float shakeIntensity;
  private float duration;
  private float elapsed;
  private float targetZoom = 1;

  //force singleton
  private CameraManager() {

  }

  public static CameraManager getInstance() {
    return instance;
  }

  public void init(OrthographicCamera camera) {
    this.camera = camera;

    Player player = Player.getInstance();
    this.screenPositionComponent = player.getComponent(ScreenPositionComponent.class);
    this.positionComponent = player.getComponent(PositionComponent.class);
  }

  public void shake(float intensity, float duration) {
    if(Settings.getInstance().cameraShaking) {
      this.shakeIntensity = intensity;
      this.duration = duration;
    }
  }

  public void updateTargetZoom(float delta) {
    targetZoom += delta;
  }

  public void setTargetZoom(float zoom) {
    targetZoom = zoom;
  }

  public void reset() {
    targetZoom = 1;
    this.camera.zoom = 1;
  }

  public void update(float deltaTime) {
    updateZoom();

    float x = Math.round(positionComponent.x);
    float y = Math.round(positionComponent.y);

    float centerX = screenPositionComponent.getDefaultX();
    float centerY = screenPositionComponent.getDefaultY();

    camera.position.x = x;
    camera.position.y = y;

    screenPositionComponent.setX(centerX);
    screenPositionComponent.setY(centerY);

    checkShakeEffect(deltaTime);

    camera.update();
  }

  // ---------------------- Helper ------------------------------------------------


  private boolean checkShakeEffect(float delta) {
    // Only shake when required.
    if(elapsed * 1000 < duration) {
      // Calculate the amount of shake based on how long it has been shaking already
      float currentPower = shakeIntensity * camera.zoom * ((duration - elapsed) / duration);
      float min = -1f;
      float max = 1f;

      Random rand = new Random();
      float x = (rand.nextFloat() * (max - min) + min) * currentPower;
      rand = new Random();
      float y = (rand.nextFloat() * (max - min) + min) * currentPower;
      camera.translate(-x, -y);

      // Increase the elapsed time by the delta provided.
      elapsed += delta;
      return true;
    }
    else {
      this.duration = 0;
      this.elapsed = 0;
    }

    return false;
  }

  private void updateZoom() {
    if(camera.zoom < targetZoom) {
      camera.zoom += ZOOM_DELTA;
    }
    if(camera.zoom > targetZoom) {
      camera.zoom -= ZOOM_DELTA;
    }
  }
}