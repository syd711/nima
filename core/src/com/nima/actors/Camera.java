package com.nima.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.nima.components.PositionComponent;
import com.nima.components.ScreenPositionComponent;
import com.nima.util.Settings;

public class Camera implements Updateable {
  private OrthographicCamera camera;

  private float worldWidth;
  private float worldHeight;
  private PositionComponent positionComponent;
  private ScreenPositionComponent screenPositionComponent;

  public Camera(OrthographicCamera camera, Player player) {
    this.camera = camera;

    this.screenPositionComponent = player.getComponent(ScreenPositionComponent.class);
    this.positionComponent = player.getComponent(PositionComponent.class);
    this.worldWidth = Settings.WORLD_WIDTH * Settings.FRAME_PIXELS_X;
    this.worldHeight = Settings.WORLD_HEIGHT * Settings.FRAME_PIXELS_Y;
  }

  @Override
  public void update() {
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
  }
}