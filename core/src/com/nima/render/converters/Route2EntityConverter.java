package com.nima.render.converters;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.nima.actors.Route;
import com.nima.components.RouteComponent;
import com.nima.data.DataEntities;
import com.nima.data.ShipProfile;
import com.nima.managers.EntityManager;
import com.nima.render.MapConstants;
import com.nima.render.TiledMapFragment;

import static com.nima.render.MapConstants.PROPERTY_OBJECT_TYPE;

/**
 * Initializes the map positions of route stations.
 */
public class Route2EntityConverter extends DefaultMapObjectConverter {

  @Override
  public boolean isApplicable(TiledMapFragment mapFragment, MapObject mapObject) {
    String type = (String) mapObject.getProperties().get(PROPERTY_OBJECT_TYPE);
    return type != null && type.equals(MapConstants.TYPE_ROUTE_POINT);
  }

  @Override
  public void convertMapObject(TiledMapFragment mapFragment, MapObject mapObject) {
    String name = mapObject.getName();

    Vector2 centeredPosition = (Vector2) mapObject.getProperties().get(MapConstants.PROPERTY_CENTERED_POSITION);
    String shipProfile = (String) mapObject.getProperties().get(MapConstants.PROPERTY_SHIP_PROFILE);

    //apply additional route tracking point
    Route route = getOrCreateRoute(name);
    route.routeComponent.routeCoordinates.add(centeredPosition);

    //check if the point contains the ship type and therefore the start point
    if(shipProfile != null) {
      ShipProfile ship = DataEntities.getShip(shipProfile);
      route.routeComponent.shipProfile = ship;
      route.routeComponent.spawnPoint = centeredPosition;
    }
  }

  private Route getOrCreateRoute(String name) {
    ImmutableArray<Entity> entitiesFor = EntityManager.getInstance().getEntitiesFor(RouteComponent.class);
    for(Entity entity : entitiesFor) {
      if(entity instanceof Route) {
        Route route = (Route) entity;
        if(route.routeComponent.name.equals(name)) {
          return route;
        }
      }
    }

    Route route = new Route(name);
    EntityManager.getInstance().add(route);
    return route;
  }
}
