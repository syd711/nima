package com.nima.managers;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.nima.actors.Location;
import com.nima.components.BodyComponent;
import com.nima.components.LocationComponent;
import com.nima.components.MapObjectComponent;

import java.util.logging.Logger;

import static com.nima.render.MapConstants.*;

/**
 * Uses MapObjects to create Ashley entities from them.
 */
public class EntityFactory {
  private static final Logger LOG = Logger.getLogger(EntityFactory.class.getName());

  public static Entity createEntity(TiledMap map, MapObject mapObject, RayHandler rayHandler) {
    String entityType = (String) mapObject.getProperties().get(PROPERTY_OBJECT_TYPE);
    if(entityType == null) {
      LOG.warning("Undefined map object " + mapObject.getProperties().get("id") + " found on " + map);
      return null;
    }


    Entity entity = null;
   if(entityType.equals(TYPE_PLANET) || entityType.equals(TYPE_STATION)) {
      entity = new Location();
      entity.add(new MapObjectComponent(mapObject));
      entity.add(new BodyComponent(mapObject));
      entity.add(new LocationComponent(mapObject));
    }
    else {
      throw new UnsupportedOperationException("Unsupported map object " + mapObject.getProperties().get("id") + " found on " + map);
    }

    return entity;
  }
}
