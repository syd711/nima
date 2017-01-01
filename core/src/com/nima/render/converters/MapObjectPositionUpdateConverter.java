package com.nima.render.converters;

import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.nima.render.MapObjectConverter;
import com.nima.render.TiledMapFragment;

/**
 * Updates the position of the map objects, depending on the fragment they are.
 */
public class MapObjectPositionUpdateConverter extends MapObjectConverter {

  @Override
  public void convertRectangle(TiledMapFragment mapFragment, RectangleMapObject mapObject) {
    Rectangle r = mapObject.getRectangle();
    r.setPosition(mapFragment.getXOffset() + r.getX(), mapFragment.getYOffset() + r.getY());
  }

  @Override
  public void convertPolygon(TiledMapFragment mapFragment, PolygonMapObject mapObject) {
    Polygon polygon = mapObject.getPolygon();
    polygon.setPosition(mapFragment.getXOffset() + polygon.getX(), mapFragment.getYOffset() + polygon.getY());
  }

  @Override
  public void convertPolyline(TiledMapFragment mapFragment, PolylineMapObject mapObject) {

  }

  @Override
  public void convertEllipse(TiledMapFragment mapFragment, EllipseMapObject mapObject) {
    Ellipse circle = mapObject.getEllipse();
    circle.setPosition(mapFragment.getXOffset() + circle.x, mapFragment.getYOffset() + circle.y);
  }
}
