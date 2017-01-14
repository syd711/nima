package com.nima.render.converters;

import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.nima.render.MapObjectConverter;
import com.nima.render.TiledMapFragment;

/**
 * Store the updated position
 */
public class MapObjectPositionConverter extends MapObjectConverter {

  @Override
  public void init(TiledMapFragment map) {
  }

  @Override
  public void convertRectangle(TiledMapFragment mapFragment, RectangleMapObject mapObject) {
    Rectangle r = mapObject.getRectangle();
    Vector2 position = r.getPosition(new Vector2());
    mapObject.getProperties().put(MapConstants.PROPERTY_POSITION, position);
  }

  @Override
  public void convertPolygon(TiledMapFragment mapFragment, PolygonMapObject mapObject) {
    Polygon polygon = mapObject.getPolygon();
    Vector2 position = new Vector2(polygon.getX(), polygon.getY());
    mapObject.getProperties().put(MapConstants.PROPERTY_POSITION, position);
  }

  @Override
  public void convertPolyline(TiledMapFragment mapFragment, PolylineMapObject mapObject) {

  }

  @Override
  public void convertEllipse(TiledMapFragment mapFragment, EllipseMapObject mapObject) {
    Ellipse circle = mapObject.getEllipse();
    Vector2 position = new Vector2(circle.x, circle.y);
    mapObject.getProperties().put(MapConstants.PROPERTY_POSITION, position);
  }
}
