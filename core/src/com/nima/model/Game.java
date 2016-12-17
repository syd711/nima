package com.nima.model;

import com.nima.render.ActorBasedTiledMultiMapRenderer;

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


  public void updateGameWorld() {
  }


}
