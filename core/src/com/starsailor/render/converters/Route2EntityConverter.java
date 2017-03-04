package com.starsailor.render.converters;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.route.Route;
import com.starsailor.actors.route.RoutePoint;
import com.starsailor.components.RouteComponent;
import com.starsailor.managers.EntityManager;
import com.starsailor.render.TiledMapFragment;

import static com.starsailor.render.converters.MapConstants.PROPERTY_OBJECT_TYPE;

/**
 * Initializes the map positions of route stations.
 */
public class Route2EntityConverter extends DefaultMapObjectConverter {

  private boolean enabled;

  public Route2EntityConverter(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public boolean isApplicable(TiledMapFragment mapFragment, MapObject mapObject) {
    String type = (String) mapObject.getProperties().get(PROPERTY_OBJECT_TYPE);
    return type != null && type.equalsIgnoreCase(MapConstants.TYPE_ROUTE);
  }

  @Override
  public void convertMapObject(TiledMapFragment mapFragment, MapObject mapObject) {
    if(!enabled) {
      return;
    }

    String name = mapObject.getName();

    Vector2 centeredPosition = (Vector2) mapObject.getProperties().get(MapConstants.PROPERTY_CENTERED_POSITION);
    Float dockTime = (Float) mapObject.getProperties().get(MapConstants.PROPERTY_DOCK_TIME);
    Boolean dockable = (Boolean) mapObject.getProperties().get(MapConstants.PROPERTY_DOCKABLE);
    Integer index = (Integer) mapObject.getProperties().get(MapConstants.PROPERTY_INDEX);

    //apply additional route tracking point
    Route route = getOrCreateRoute(name);

    RoutePoint routePoint = new RoutePoint(index);
    routePoint.setPosition(centeredPosition);
    routePoint.setDockable(dockable != null && dockable);
    routePoint.setDockTime(dockTime);
    route.routeComponent.getRoutePoints().add(routePoint);
  }

  /**
   * Checks if a route entity exists for the given name or creates a new one
   */
  private Route getOrCreateRoute(String name) {
    ImmutableArray<Entity> entitiesFor = EntityManager.getInstance().getEntitiesFor(RouteComponent.class);
    for(Entity entity : entitiesFor) {
      if(entity instanceof Route) {
        Route route = (Route) entity;
        if(route.getName().equals(name)) {
          return route;
        }
      }
    }

    Route route = new Route(name);
    EntityManager.getInstance().add(route);
    return route;
  }
}
