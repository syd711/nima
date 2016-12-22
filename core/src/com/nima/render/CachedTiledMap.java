package com.nima.render;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.nima.util.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * The cached map with additional information to optimize caching
 */
public class CachedTiledMap {
  private static final Logger LOG = Logger.getLogger(CachedTiledMap.class.getName());

  private boolean rendered;
  private TiledMap map;

  private int frameNumberX;
  private int frameNumberY;

  private String filename;

  private List<MapObject> mapObjects = new ArrayList<>();

  protected CachedTiledMap(TmxCacheMapLoader loader) {
    this.map = loader.getMap();
    this.filename = loader.getFilename();
    this.frameNumberX = loader.getFrameX();
    this.frameNumberY = loader.getFrameY();
    renderObjects();
  }

  public List<MapObject> getMapObjects() {
    return mapObjects;
  }

  private void renderObjects() {
    if(!rendered) {
      rendered = true;

      float xOffset = frameNumberX * Settings.FRAME_PIXELS_X;
      float yOffset = frameNumberY * Settings.FRAME_PIXELS_Y;

      for(MapLayer mapLayer : map.getLayers()) {
        MapObjects objects = mapLayer.getObjects();
        for(MapObject object : objects) {
          renderObject(object, xOffset, yOffset);
          mapObjects.add(object);
        }
      }

      LOG.info("Rendered objects of frame " + frameNumberX + ","  + frameNumberY);
    }
  }

  /**
   * Update the position of the objects, depending
   * on the active frame.
   */
  private void renderObject(MapObject object, float xOffset, float yOffset) {
    if(object instanceof RectangleMapObject) {
      RectangleMapObject rectangle = (RectangleMapObject) object;
      Rectangle r = rectangle.getRectangle();
      r.setPosition(xOffset + r.getX(), yOffset + r.getY());
    }
    else if(object instanceof PolygonMapObject) {
    }
    else if(object instanceof PolylineMapObject) {
    }
    else if(object instanceof CircleMapObject) {
    }
  }

  public TiledMap getMap() {
    return map;
  }

  public String getFilename() {
    return filename;
  }


  @Override
  public boolean equals(Object obj) {
    return getFilename().equals(((CachedTiledMap)obj).getFilename());
  }
}
