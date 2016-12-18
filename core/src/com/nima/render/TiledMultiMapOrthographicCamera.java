package com.nima.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.nima.util.Settings;

/**
 * Camera that is bound to the actor of the ActorBasedTiledMap.
 */
public class TiledMultiMapOrthographicCamera {

  private final OrthographicCamera camera;
  private final ActorBasedTiledMultiMapRenderer renderer;

  private float worldWidth;
  private float worldHeight;

  public TiledMultiMapOrthographicCamera(ActorBasedTiledMultiMapRenderer renderer, OrthographicCamera camera) {
    this.camera = camera;
    this.renderer = renderer;

    this.worldWidth = Settings.WORLD_WIDTH * renderer.framePixelsX;
    this.worldHeight = Settings.WORLD_HEIGHT * renderer.framePixelsY;
  }

  public void updateCamera() {
    boolean keepX = false;
    boolean keepY = false;

    float width = Gdx.graphics.getWidth();
    float height = Gdx.graphics.getHeight();

    //x left
    if(renderer.mainActor.getX()<(width/2)) {
      keepX = true;
    }

    //x right
    if(renderer.mainActor.getX() > (worldWidth-(width/2))) {
      keepX = true;
    }

    //y bottom
    if(renderer.mainActor.getY()<(height/2)) {
      keepY = true;
    }

    //y top
    if(renderer.mainActor.getY() > (worldHeight-(height/2))) {
      keepY = true;
    }

    if(!keepX) {
      camera.position.x = renderer.mainActor.getX();
    }
    if(!keepY) {
      camera.position.y = renderer.mainActor.getY();
    }
  }
}
