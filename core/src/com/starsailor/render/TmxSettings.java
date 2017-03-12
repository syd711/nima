package com.starsailor.render;

import com.badlogic.gdx.maps.MapProperties;

/**
 * All constants required for map loading
 */
public class TmxSettings {

  //starting from 1
  public final static int WORLD_WIDTH = 4;
  public final static int WORLD_HEIGHT = 4;

  //starting from 0
  public final static int START_FRAME_X = 0;
  public final static int START_FRAME_Y = 0;

  /**
   * Map Settings
   */
  public static int FRAME_PIXELS_X = 0;
  public static int FRAME_PIXELS_Y = 0;

  public static int WORLD_PIXELS_X = 0;
  public static int WORLD_PIXELS_Y = 0;

  public static void init(MapProperties mapProperties) {
    int width = (int) mapProperties.get("width");
    int height = (int) mapProperties.get("height");
    int tileWidth = (int) mapProperties.get("tilewidth");
    int tileHeight = (int) mapProperties.get("tileheight");


    FRAME_PIXELS_X = width * tileWidth;
    FRAME_PIXELS_Y = height * tileHeight;

    WORLD_PIXELS_X = FRAME_PIXELS_X * WORLD_WIDTH;
    WORLD_PIXELS_Y = FRAME_PIXELS_Y * WORLD_HEIGHT;
  }


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
