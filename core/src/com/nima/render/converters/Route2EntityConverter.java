package com.nima.render.converters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.nima.actors.Route;
import com.nima.data.DataEntities;
import com.nima.data.RouteProfile;
import com.nima.managers.EntityManager;
import com.nima.render.MapConstants;
import com.nima.render.TiledMapFragment;

import java.util.List;

import static com.nima.render.MapConstants.PROPERTY_OBJECT_TYPE;

/**
 * Initializes the map positions of route stations.
 */
public class Route2EntityConverter extends DefaultMapObjectConverter {

  @Override
  public boolean isApplicable(TiledMapFragment mapFragment, MapObject mapObject) {
    String type = (String) mapObject.getProperties().get(PROPERTY_OBJECT_TYPE);
    return type != null && (type.equals(MapConstants.TYPE_PLANET) || type.equals(MapConstants.TYPE_STATION));
  }

  @Override
  public void convertMapObject(TiledMapFragment mapFragment, MapObject mapObject) {
    String name = mapObject.getName();
    List<RouteProfile> routesForLocation = DataEntities.getRoutesForLocation(name);
    for(RouteProfile routeProfile : routesForLocation) {
      if(!EntityManager.getInstance().isRouteActive(routeProfile)) {
        Route route = new Route(routeProfile);
        EntityManager.getInstance().add(route);
        Gdx.app.log("Route2EntityConverter", "Activated route '" + routeProfile.name + "'");
      }
    }
  }
}
