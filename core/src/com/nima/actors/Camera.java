package com.nima.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.nima.components.PositionComponent;
import com.nima.util.Settings;

public class Camera implements Updateable {
  private OrthographicCamera camera;
  private PositionComponent positionComponent;

  private float worldWidth;
  private float worldHeight;

  public Camera(OrthographicCamera camera, PositionComponent positionComponent) {
    this.camera = camera;
    this.positionComponent = positionComponent;

    this.worldWidth = Settings.WORLD_WIDTH * Settings.FRAME_PIXELS_X;
    this.worldHeight = Settings.WORLD_HEIGHT * Settings.FRAME_PIXELS_Y;
  }

  @Override
  public void update() {
    float x = positionComponent.x;
    float y = positionComponent.y;

    boolean keepX = false;
    boolean keepY = false;

    float width = Gdx.graphics.getWidth();
    float height = Gdx.graphics.getHeight();

    //x left
    if(x < (width / 2)) {
      keepX = true;
    }

    //x right
    if(x > (worldWidth - (width / 2))) {
      keepX = true;
    }

    //y bottom
    if(y < (height / 2)) {
      keepY = true;
    }

    //y top
    if(y > (worldHeight - (height / 2))) {
      keepY = true;
    }

    if(!keepX) {
      camera.position.x = x;
    }
    if(!keepY) {
      camera.position.y = y;
    }
  }
}