package com.nima.actors;

import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;

/**
 * Common superclass for all NPC.
 * We assume that they are instances of Spine.
 */
public class NPC extends Spine {

  public NPC(BatchTiledMapRenderer renderer, String path, String defaultAnimation, float jsonScaling, float x, float y) {
    super(renderer, path, defaultAnimation, jsonScaling, x, y);
  }

}
