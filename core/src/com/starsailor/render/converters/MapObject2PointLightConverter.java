package com.starsailor.render.converters;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.GameEntity;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.LightComponent;
import com.starsailor.managers.EntityManager;
import com.starsailor.render.TiledMapFragment;

import static com.starsailor.render.converters.MapConstants.PROPERTY_LIGHT_DISTANCE;
import static com.starsailor.render.converters.MapConstants.PROPERTY_OBJECT_TYPE;

/**
 * Store the updated position
 */
public class MapObject2PointLightConverter extends DefaultMapObjectConverter {
  private final RayHandler rayHandler;

  public MapObject2PointLightConverter(RayHandler rayHandler) {
    this.rayHandler = rayHandler;
  }

  @Override
  public boolean isApplicable(TiledMapFragment mapFragment, MapObject mapObject) {
    String type = (String) mapObject.getProperties().get(PROPERTY_OBJECT_TYPE);
    return type != null && type.equalsIgnoreCase(MapConstants.TYPE_POINT_LIGHT);
  }

  @Override
  public void convertEllipse(TiledMapFragment mapFragment, EllipseMapObject mapObject) {
    Ellipse ellipse = mapObject.getEllipse();
    Vector2 centeredPosition = (Vector2) mapObject.getProperties().get(MapConstants.PROPERTY_CENTERED_POSITION);

    float distance = getProperty(mapObject, PROPERTY_LIGHT_DISTANCE, ellipse.width);

    Entity entity = new GameEntity();
    LightComponent component =  ComponentFactory.addLightComponent(entity);
    component.init(rayHandler, distance, centeredPosition.x, centeredPosition.y, false);

    ComponentFactory.addMapObjectComponent(entity, mapObject);
    EntityManager.getInstance().add(entity);

  }
}
