package com.starsailor.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.starsailor.actors.Player;
import com.starsailor.components.PositionComponent;
import com.starsailor.components.ScreenPositionComponent;
import com.starsailor.util.Settings;

import java.util.Random;

public class CameraManager {
  private OrthographicCamera camera;

  private float worldWidth;
  private float worldHeight;
  private PositionComponent positionComponent;
  private ScreenPositionComponent screenPositionComponent;

  private static CameraManager instance = new CameraManager();

  //variables used for a shaking effect
  private float shakeIntensity;
  private float duration;
  private float elapsed;

  //force singleton
  private CameraManager() {

  }

  public static CameraManager getInstance() {
    return instance;
  }

  public void init(OrthographicCamera camera, Player player) {
    this.camera = camera;

    this.screenPositionComponent = player.getComponent(ScreenPositionComponent.class);
    this.positionComponent = player.getComponent(PositionComponent.class);
    this.worldWidth = Settings.WORLD_WIDTH * Settings.FRAME_PIXELS_X;
    this.worldHeight = Settings.WORLD_HEIGHT * Settings.FRAME_PIXELS_Y;
  }

  public void shake(float intensity, float duration) {
    if(Settings.getInstance().cameraShaking) {
      this.shakeIntensity = intensity;
      this.duration = duration;
    }
  }

  public void update(float deltaTime) {

    float x = Math.round(positionComponent.x);
    float y = Math.round(positionComponent.y);

    boolean keepX = false;
    boolean keepY = false;

    float width = Gdx.graphics.getWidth();
    float height = Gdx.graphics.getHeight();

    float centerX = screenPositionComponent.getDefaultX();
    float centerY = screenPositionComponent.getDefaultY();

    //x left
    if(x < (width / 2)) {
      centerX = x;
      keepX = true;
    }

    //x right
    if(x > (worldWidth - (width / 2))) {
      centerX = x%Settings.FRAME_PIXELS_X;
      keepX = true;
    }

    //y bottom
    if(y < (height / 2)) {
      centerY = y;
      keepY = true;
    }

    //y top
    if(y > (worldHeight - (height / 2))) {
      centerY = y%Settings.FRAME_PIXELS_Y;
      keepY = true;
    }

    if(!keepX) {
      camera.position.x = x;
    }

    if(!keepY) {
      camera.position.y = y;
    }

    screenPositionComponent.setX(centerX);
    screenPositionComponent.setY(centerY);

    checkShakeEffect(deltaTime);

    camera.update();
  }


  public boolean checkShakeEffect(float delta) {
    // Only shake when required.
    if(elapsed*1000 < duration) {
      // Calculate the amount of shake based on how long it has been shaking already
      float currentPower = shakeIntensity * camera.zoom * ((duration - elapsed) / duration);
      float min = -1f;
      float max = 1f;

      Random rand = new Random();
      float x = (rand.nextFloat() * (max - min) + min)*currentPower;
      rand = new Random();
      float y = (rand.nextFloat() * (max - min) + min)*currentPower;
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
}