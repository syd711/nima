package com.starsailor.render;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The cached map with additional information to optimize caching
 */
public class TiledMapFragment {
  private TiledMap map;

  private int frameNumberX;
  private int frameNumberY;

  private TiledMultiMapRenderer renderer;
  private File tmxFile;

  private boolean dirty = true;

  private List<MapObject> mapObjects = new ArrayList<>();

  protected TiledMapFragment(TiledMultiMapRenderer renderer, File tmxFile, int x, int y) {
    this.renderer = renderer;
    this.tmxFile = tmxFile;
    this.dirty = true;

    TmxCacheMapLoader loader = new TmxCacheMapLoader(tmxFile, x, y);
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
    return frameNumberX * renderer.getFramePixelsX();
  }

  public float getYOffset() {
    return frameNumberY * renderer.getFramePixelsX();
  }

  public TiledMap getMap() {
    return map;
  }

  @Override
  public String toString() {
    return tmxFile.getName();
  }

  public boolean isDirty() {
    return dirty;
  }

  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }
}
