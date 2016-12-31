package com.nima.managers;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.nima.actors.Location;
import com.nima.components.*;
import com.nima.render.MapConstants;

import java.util.logging.Logger;

import static com.nima.render.MapConstants.*;

/**
 * Uses MapObjects to create Ashley entities from them.
 */
public class EntityFactory {
  private static final Logger LOG = Logger.getLogger(EntityFactory.class.getName());

  private static final float DEFAULT_LIGHT_DISTANCE = 300;
  private static final float DEFAULT_LIGHT_DEGREE = 45;
  private static final float DEFAULT_CONE_DEGREE = 45;

  public static Entity createEntity(TiledMap map, MapObject mapObject, RayHandler rayHandler) {
    String entityType = (String) mapObject.getProperties().get(PROPERTY_OBJECT_TYPE);
    if(entityType == null) {
      LOG.warning("Undefined map object " + mapObject.getProperties().get("id") + " found on " + map);
      new CollisionComponent(mapObject);
      return null;
    }

    Vector2 centeredPosition = (Vector2) mapObject.getProperties().get(MapConstants.PROPERTY_CENTERED_POSITION);

    Entity entity = null;
    if(entityType.equals(TYPE_POINT_LIGHT)) {
      entity = new Entity();
      float distance = getProperty(mapObject, PROPERTY_LIGHT_DISTANCE, DEFAULT_LIGHT_DISTANCE);
      entity.add(new LightComponent(rayHandler, distance, centeredPosition.x, centeredPosition.y, false));
      entity.add(new MapObjectComponent(mapObject));
    }
    else if(entityType.equals(TYPE_CONE_LIGHT)) {
      entity = new Entity();
      float distance = getProperty(mapObject, PROPERTY_LIGHT_DISTANCE, DEFAULT_LIGHT_DISTANCE);
      float degree = getProperty(mapObject, PROPERTY_LIGHT_DEGREE, DEFAULT_LIGHT_DEGREE);
      float coneDegree = getProperty(mapObject, PROPERTY_CONE_DEGREE, DEFAULT_CONE_DEGREE);
      entity.add(new LightComponent(rayHandler, distance, centeredPosition.x, centeredPosition.y, degree, coneDegree, false));
      entity.add(new MapObjectComponent(mapObject));
    }
    else if(entityType.equals(TYPE_PLANET) || entityType.equals(TYPE_STATION)) {
      entity = new Location();
      entity.add(new MapObjectComponent(mapObject));
      entity.add(new BodyComponent(mapObject));
      entity.add(new LocationComponent(mapObject));
      entity.add(new CollisionComponent(mapObject));
    }
    else {
      throw new UnsupportedOperationException("Unsupported map object " + mapObject.getProperties().get("id") + " found on " + map);
    }

    return entity;
  }

  private static float getProperty(MapObject mapObject, String property, float defaultValue) {
    if(mapObject.getProperties().containsKey(property)) {
      return Float.valueOf(String.valueOf(mapObject.getProperties().get(property)));
    }
    return defaultValue;
  }
}
