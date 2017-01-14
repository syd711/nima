package com.nima.render.converters;

import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.nima.render.MapObjectConverter;
import com.nima.render.TiledMapFragment;
import com.nima.util.PolygonUtil;

/**
 * Stores the centered position of the map object.
 */
public class MapObjectCenteredPositionConverter extends MapObjectConverter {
  @Override
  public void init(TiledMapFragment map) {
  }

  @Override
  public void convertRectangle(TiledMapFragment mapFragment, RectangleMapObject mapObject) {
    Rectangle r = mapObject.getRectangle();
    Vector2 position = r.getPosition(new Vector2());
    Vector2 centeredPosition = new Vector2(position.x+r.width/2, position.y+r.height/2);
    mapObject.getProperties().put(MapConstants.PROPERTY_CENTERED_POSITION, centeredPosition);
  }

  @Override
  public void convertPolygon(TiledMapFragment mapFragment, PolygonMapObject mapObject) {
    Vector2 centeredPosition = PolygonUtil.getCenter(mapObject.getPolygon());
    mapObject.getProperties().put(MapConstants.PROPERTY_CENTERED_POSITION, centeredPosition);
  }

  @Override
  public void convertPolyline(TiledMapFragment mapFragment, PolylineMapObject mapObject) {

  }

  @Override
  public void convertEllipse(TiledMapFragment mapFragment, EllipseMapObject mapObject) {
    Ellipse circle = mapObject.getEllipse();
    Vector2 centeredPosition = new Vector2(circle.x + circle.width / 2, circle.y + circle.height / 2);
    mapObject.getProperties().put(MapConstants.PROPERTY_CENTERED_POSITION, centeredPosition);
  }
}
