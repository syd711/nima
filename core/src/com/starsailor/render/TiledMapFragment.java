package com.starsailor.render;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;

import java.util.ArrayList;
import java.util.List;

/**
 * The cached map with additional information to optimize caching
 */
public class TiledMapFragment {
  private TiledMap map;

  private int frameNumberX;
  private int frameNumberY;

  private List<MapObject> mapObjects = new ArrayList<>();

  protected TiledMapFragment(int x, int y) {
    TmxCacheMapLoader loader = new TmxCacheMapLoader(x, y);
    this.map = loader.getMap();
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
    return frameNumberX * TmxSettings.FRAME_PIXELS_X;
  }

  public float getYOffset() {
    return frameNumberY * TmxSettings.FRAME_PIXELS_Y;
  }

  public TiledMap getMap() {
    return map;
  }

  @Override
  public String toString() {
    return "Map Fragment {" + frameNumberX +"/" + frameNumberY + "}";
  }
}
