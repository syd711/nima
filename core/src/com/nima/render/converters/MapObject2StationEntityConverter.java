package com.nima.render.converters;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.physics.box2d.Body;
import com.nima.actors.Location;
import com.nima.components.BodyComponent;
import com.nima.components.LocationComponent;
import com.nima.components.MapObjectComponent;
import com.nima.managers.EntityManager;
import com.nima.render.MapConstants;
import com.nima.render.MapObjectConverter;
import com.nima.render.TiledMapFragment;

import static com.nima.render.MapConstants.PROPERTY_OBJECT_TYPE;

/**
 * Store the updated position
 */
public class MapObject2StationEntityConverter extends MapObjectConverter {

  @Override
  public boolean isApplicable(TiledMapFragment mapFragment, MapObject mapObject) {
    String type = (String) mapObject.getProperties().get(PROPERTY_OBJECT_TYPE);
    return type != null && (type.equals(MapConstants.TYPE_PLANET) || type.equals(MapConstants.TYPE_STATION));
  }

  @Override
  public void convertRectangle(TiledMapFragment mapFragment, RectangleMapObject mapObject) {
    createStationEntity(mapObject);
  }

  @Override
  public void convertPolygon(TiledMapFragment mapFragment, PolygonMapObject mapObject) {
    createStationEntity(mapObject);
  }

  @Override
  public void convertPolyline(TiledMapFragment mapFragment, PolylineMapObject mapObject) {
    createStationEntity(mapObject);
  }

  @Override
  public void convertEllipse(TiledMapFragment mapFragment, EllipseMapObject mapObject) {
    createStationEntity(mapObject);
  }

  private void createStationEntity(MapObject mapObject) {
    Entity entity = new Location();
    entity.add(new MapObjectComponent(mapObject));
    entity.add(new BodyComponent(mapObject));
    entity.add(new LocationComponent(mapObject));

    Body body = (Body) mapObject.getProperties().get(MapConstants.PROPERTY_COLLISION_COMPONENT);
    body.setUserData(entity);

    EntityManager.getInstance().add(entity);
  }
}
