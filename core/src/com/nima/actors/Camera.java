package com.nima.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.nima.util.Settings;

public class Camera implements Updateable {
  private OrthographicCamera camera;

  private float worldWidth;
  private float worldHeight;
  private Player player;

  public Camera(OrthographicCamera camera, Player player) {
    this.camera = camera;
    this.player = player;
    this.worldWidth = Settings.WORLD_WIDTH * Settings.FRAME_PIXELS_X;
    this.worldHeight = Settings.WORLD_HEIGHT * Settings.FRAME_PIXELS_Y;
  }

  @Override
  public void update() {
    float x = Math.round(player.getPositionComponent().x);
    float y = Math.round(player.getPositionComponent().y);

    boolean keepX = false;
    boolean keepY = false;

    float width = Gdx.graphics.getWidth();
    float height = Gdx.graphics.getHeight();

    float centerX = player.getScreenPosition().getDefaultX();
    float centerY = player.getScreenPosition().getDefaultY();

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

    player.getScreenPosition().setX(centerX);
    player.getScreenPosition().setY(centerY);
  }
}