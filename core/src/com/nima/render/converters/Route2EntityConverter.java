package com.nima.render.converters;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.nima.actors.Route;
import com.nima.components.RouteComponent;
import com.nima.data.DataEntities;
import com.nima.data.RoutePoint;
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
    Integer dockTime = (Integer) mapObject.getProperties().get(MapConstants.PROPERTY_DOCK_TIME);
    Boolean dockable = (Boolean) mapObject.getProperties().get(MapConstants.PROPERTY_DOCKABLE);

    //apply additional route tracking point
    Route route = getOrCreateRoute(name);

    RoutePoint routePoint = new RoutePoint();
    routePoint.position = centeredPosition;
    routePoint.dockable = dockable != null && dockable;
    routePoint.dockTime = dockTime;
    route.routeComponent.routeCoordinates.add(routePoint);

    //check if the point contains the ship type and therefore the start point
    if(shipProfile != null) {
      ShipProfile ship = DataEntities.getShip(shipProfile);
      route.routeComponent.shipProfile = ship;
      route.routeComponent.spawnPoint = routePoint;
    }
  }

  /**
   * Checks if a route entity exists for the given name or creates a new one
   */
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
