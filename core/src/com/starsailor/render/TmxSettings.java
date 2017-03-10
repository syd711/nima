package com.starsailor.render;

/**
 * All constants required for map loading
 */
public class TmxSettings {

  //starting from 1
  public final static int WORLD_WIDTH = 2;
  public final static int WORLD_HEIGHT = 2;

  //starting from 0
  public final static int START_FRAME_X = 0;
  public final static int START_FRAME_Y = 0;


  /**
   * Map Settings
   */
  public final static int FRAME_PIXELS_X = 1920;
  public final static int FRAME_PIXELS_Y = 1280;


  private static String MAIN_MAP_FOLDER = "maps/main/";
  private static String MAIN_MAP_PREFIX = "main_";

  /**
   * Helper for generating the cache key
   */
  protected static String keyFor(int x, int y) {
    String key = MAIN_MAP_PREFIX + x + "," + y + ".tmx";
    if(MAIN_MAP_FOLDER != null) {
      if(!MAIN_MAP_FOLDER.endsWith("/")) {
        MAIN_MAP_FOLDER += "/";
      }
      key = MAIN_MAP_FOLDER + key;
    }
    return key;
  }
}
