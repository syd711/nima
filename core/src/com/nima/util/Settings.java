package com.nima.util;

/**
 * Some game settings constants
 */
public class Settings {

  public static final boolean DEBUG = true;
  public static final boolean SOUND_ENABLED = false;
  public static final float DOCKING_TARGET_SCALE = 0.4f;

  /**
   * Light Settings
   */
  public static final float AMBIENT_LIGHT_BRIGHTNESS = 0.8f;
  public static final float FADE_OUT_OFFSET = 0.01f;
  public static final float FADE_IN_OFFSET = 0.01f;

  /**
   * Map Settings
   */
  public static final int FRAME_PIXELS_X = 1920;
  public static final int FRAME_PIXELS_Y = 1280;

  //starting from 1
  public static final int WORLD_WIDTH = 2;
  public static final int WORLD_HEIGHT = 2;

  //starting from 0
  public static final int START_FRAME_X = 0;
  public static final int START_FRAME_Y = 0;

  /**
   * Box2d Settings
   */
  public final static float MPP = 0.01f;
  public final static int PPM = 100;

  /**
   * Bullets
   */
  public final static float BULLET_AUTO_DESTROY_DISTANCE = 4000;//px
}
