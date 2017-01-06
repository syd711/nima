package com.nima.render.converters;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.nima.components.LightComponent;
import com.nima.managers.EntityManager;
import com.nima.render.MapConstants;
import com.nima.render.MapObjectConverter;
import com.nima.render.TiledMapFragment;

import static com.nima.render.MapConstants.PROPERTY_LIGHT_DISTANCE;
import static com.nima.render.MapConstants.PROPERTY_OBJECT_TYPE;

/**
 * Store the updated position
 */
public class MapObject2PointLightConverter extends MapObjectConverter {
  private static final float DEFAULT_LIGHT_DISTANCE = 300;

  private final RayHandler rayHandler;

  public MapObject2PointLightConverter(RayHandler rayHandler) {
    this.rayHandler = rayHandler;
  }

  @Override
  public boolean isApplicable(TiledMapFragment mapFragment, MapObject mapObject) {
    String type = (String) mapObject.getProperties().get(PROPERTY_OBJECT_TYPE);
    return type != null && type.equals(MapConstants.TYPE_POINT_LIGHT);
  }

  @Override
  public void convertRectangle(TiledMapFragment mapFragment, RectangleMapObject mapObject) {
  }

  @Override
  public void convertPolygon(TiledMapFragment mapFragment, PolygonMapObject mapObject) {
  }

  @Override
  public void convertPolyline(TiledMapFragment mapFragment, PolylineMapObject mapObject) {

  }

  @Override
  public void convertEllipse(TiledMapFragment mapFragment, EllipseMapObject mapObject) {
    Vector2 centeredPosition = (Vector2) mapObject.getProperties().get(MapConstants.PROPERTY_CENTERED_POSITION);

    float distance = getProperty(mapObject, PROPERTY_LIGHT_DISTANCE, DEFAULT_LIGHT_DISTANCE);

    Entity entity = new Entity();
    LightComponent component =  EntityManager.getInstance().addLightComponent(entity);
    component.init(rayHandler, distance, centeredPosition.x, centeredPosition.y, false);

    EntityManager.getInstance().addMapObjectComponent(entity, mapObject);
    EntityManager.getInstance().add(entity);

  }
}
