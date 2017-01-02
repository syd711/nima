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
import com.nima.components.MapObjectComponent;
import com.nima.managers.EntityManager;
import com.nima.render.MapConstants;
import com.nima.render.MapObjectConverter;
import com.nima.render.TiledMapFragment;

import static com.nima.render.MapConstants.*;

/**
 * Store the updated position
 */
public class MapObject2ConeLightConverter extends MapObjectConverter {
  private static final float DEFAULT_LIGHT_DISTANCE = 300;
  private static final float DEFAULT_LIGHT_DEGREE = 45;
  private static final float DEFAULT_CONE_DEGREE = 45;

  private final RayHandler rayHandler;

  public MapObject2ConeLightConverter(RayHandler rayHandler) {
    this.rayHandler = rayHandler;
  }

  @Override
  public boolean isApplicable(TiledMapFragment mapFragment, MapObject mapObject) {
    String type = (String) mapObject.getProperties().get(PROPERTY_OBJECT_TYPE);
    return type != null && type.equals(MapConstants.TYPE_CONE_LIGHT);
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
    float degree = getProperty(mapObject, PROPERTY_LIGHT_DEGREE, DEFAULT_LIGHT_DEGREE);
    float coneDegree = getProperty(mapObject, PROPERTY_CONE_DEGREE, DEFAULT_CONE_DEGREE);

    Entity entity = new Entity();
    entity.add(new LightComponent(rayHandler, distance, centeredPosition.x, centeredPosition.y, degree, coneDegree, false));
    entity.add(new MapObjectComponent(mapObject));

    EntityManager.getInstance().add(entity);
  }
}
