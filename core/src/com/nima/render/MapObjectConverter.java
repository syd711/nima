package com.nima.render;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;

/**
 * Interface to be implemented for converting tile map objects
 * into something meaningful or just to enrich them with additional data.
 */
abstract public class MapObjectConverter {

  /**
   * Just the central entry point to delegate to more specific conversion depending on the map object type.
   * @param mapFragment the map for which the map object has been loaded
   * @param mapObject the map object to convert
   */
  public void convertMapObject(TiledMapFragment mapFragment, MapObject mapObject) {
    if(!isApplicable(mapFragment, mapObject)) {
      return;
    }

    if(mapObject instanceof RectangleMapObject) {
      convertRectangle(mapFragment, (RectangleMapObject) mapObject);
    }
    else if(mapObject instanceof PolygonMapObject) {
      convertPolygon(mapFragment, (PolygonMapObject) mapObject);
    }
    else if(mapObject instanceof PolylineMapObject) {
      convertPolyline(mapFragment, (PolylineMapObject) mapObject);
    }
    else if(mapObject instanceof EllipseMapObject) {
      convertEllipse(mapFragment, (EllipseMapObject) mapObject);
    }
  }

  /**
   * Returns true if the given map object is applicable for this converter
   * @param mapFragment the map object's map
   * @param mapObject the map object to convert
   */
  public boolean isApplicable(TiledMapFragment mapFragment, MapObject mapObject) {
    return true;
  }

  public abstract void convertRectangle(TiledMapFragment mapFragment, RectangleMapObject mapObject);

  public abstract void convertPolygon(TiledMapFragment mapFragment, PolygonMapObject mapObject);

  public abstract void convertPolyline(TiledMapFragment mapFragment, PolylineMapObject mapObject);

  public abstract void convertEllipse(TiledMapFragment mapFragment, EllipseMapObject mapObject);

  /**
   * Called for every map object when the map is destroyed.
   * @param mapFragment the map fragment that has been evicted from cache
   * @param mapObject one object on the map.
   */
  public void destroy(TiledMapFragment mapFragment, MapObject mapObject) {

  }
}
