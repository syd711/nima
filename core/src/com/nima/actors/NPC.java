package com.nima.actors;

/**
 * Common superclass for all NPC.
 * We assume that they are instances of Spine.
 */
public class NPC extends Spine {

  public NPC(String path, String defaultAnimation, float jsonScaling, float x, float y) {
    super(path, defaultAnimation, jsonScaling, x, y);
  }

}
