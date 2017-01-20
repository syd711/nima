package com.starsailor.render;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.util.Settings;

import java.util.ArrayList;
import java.util.List;

/**
 * The cached map with additional information to optimize caching
 */
public class TiledMapFragment {
  private TiledMap map;

  private int frameNumberX;
  private int frameNumberY;

  private String filename;

  private List<MapObject> mapObjects = new ArrayList<>();

  protected TiledMapFragment(TmxCacheMapLoader loader) {
    this.map = loader.getMap();
    this.filename = loader.getFilename();
    this.frameNumberX = loader.getFrameX();
    this.frameNumberY = loader.getFrameY();

    for(MapLayer mapLayer : map.getLayers()) {
      MapObjects objects = mapLayer.getObjects();
      for(MapObject object : objects) {
        if (object instanceof TextureMapObject){
          continue;
        }

        mapObjects.add(object);
      }
    }
  }

  public List<MapObject> getMapObjects() {
    return mapObjects;
  }

  public float getXOffset() {
    return frameNumberX * Settings.FRAME_PIXELS_X;
  }

  public float getYOffset() {
    return frameNumberY * Settings.FRAME_PIXELS_Y;
  }

  public TiledMap getMap() {
    return map;
  }

  public String getFilename() {
    return filename;
  }

  @Override
  public boolean equals(Object obj) {
    return getFilename().equals(((TiledMapFragment)obj).getFilename());
  }

  public void containsCoordinates(List<Vector2> routeCoordinates) {

  }
}
