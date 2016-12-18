package com.nima.model;

import com.badlogic.gdx.Gdx;
import com.nima.render.ActorBasedTiledMultiMapRenderer;
import com.nima.util.Settings;

/**
 * The actual game renderer.
 * The ActorCenteredTiledMultiMapRenderer does most of the rendering magic
 * so that we simple have implement additional rendering log besides the
 * automatically multi map loading.
 */
public class Game extends ActorBasedTiledMultiMapRenderer {

  public Game(String actorLayerName, String mapFolder, String mapPrefix) {
    super(actorLayerName, mapFolder, mapPrefix);
  }

  @Override
  protected void initMainActor() {
    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    float x= Settings.START_FRAME_X*framePixelsX+(w/2);
    float y = Settings.START_FRAME_Y*framePixelsY+(h/2);
    this.mainActor.setPosition(x, 0);
  }

  @Override
  protected void renderGameWorld() {

  }

  @Override
  protected void updateGameWorld() {

  }
}
