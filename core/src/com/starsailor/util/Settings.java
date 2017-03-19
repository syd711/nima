package com.starsailor.util;

import com.starsailor.model.JsonDataFactory;

import java.io.File;

/**
 * Some game settings constants
 */
public class Settings {

  private static Settings instance;

  private Settings() {
  }

  public static Settings getInstance() {
    if(instance == null) {
      File file = new File("settings.json");
      instance = JsonDataFactory.loadDataEntity(file, Settings.class);
    }
    return instance;
  }


  public final static int START_FRAME_X = 0;
  public final static int START_FRAME_Y = 0;

  /**
   * Box2d Settings
   */
  public static final float MPP = 0.01f;
  public static final float PPM = 100f;

  /**
   * Scene2d Settings
   */
  public static final long CONTEXT_MENU_TIMEOUT = 3000;


  /**
   * Screen Settings
   */
  public String version;
  public boolean fullscreen;
  public boolean resizable;
  public int backgroundFPS;
  public int foregroundFPS;
  public int x;
  public int width;
  public int height;

  /**
   * Debugging stuff
   */
  public boolean debug;
  public boolean steering_enabled;
  public boolean npcs_enabled;
  public boolean sound_enabled;
  public float cameraZoom;
  public boolean cameraShaking = false;


  public float docking_target_scale;

  /**
   * Light Settings
   */
  public float ambient_light_brightness;
  public float fade_out_offset;
  public float fade_in_offset;

  /**
   * Bullets
   */
  public float bullet_auto_destroy_distance;

}
