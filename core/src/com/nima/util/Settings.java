package com.nima.util;

/**
 * Some game settings constants
 */
public class Settings {

  public static final boolean DEBUG = true;
  public static final float MAX_ACTOR_SPEED = 3f;
  public static final float ACTOR_ROTATION_SPEED = 2;

  /**
   * Light Settings
   */
  public static final float FADE_OUT_OFFSET = 0.01f;
  public static final float FADE_IN_OFFSET = 0.01f;

  /**
   * Map Settings
   */
  public static final String OBJECT_LAYER = "collisions";
  public static final int FRAME_PIXELS_X = 1920;
  public static final int FRAME_PIXELS_Y = 1280;

  //starting from 1
  public static final int WORLD_WIDTH = 2;
  public static final int WORLD_HEIGHT = 2;

  //starting from 0
  public static final int START_FRAME_X = 0;
  public static final int START_FRAME_Y = 0;

  public static final float PPM = 1;
}
