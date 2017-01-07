package com.nima.render.converters;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.nima.data.RouteProfiles;
import com.nima.render.MapConstants;
import com.nima.render.TiledMapFragment;

import static com.nima.render.MapConstants.PROPERTY_OBJECT_TYPE;

/**
 * Initializes the map positions of route stations.
 */
public class RoutePositionConverter extends DefaultMapObjectConverter {

  @Override
  public boolean isApplicable(TiledMapFragment mapFragment, MapObject mapObject) {
    String type = (String) mapObject.getProperties().get(PROPERTY_OBJECT_TYPE);
    return type != null && (type.equals(MapConstants.TYPE_PLANET) || type.equals(MapConstants.TYPE_STATION));
  }

  @Override
  public void convertMapObject(TiledMapFragment mapFragment, MapObject mapObject) {
    Vector2 centeredPosition = (Vector2) mapObject.getProperties().get(MapConstants.PROPERTY_CENTERED_POSITION);
    if(centeredPosition != null) {
      String name = mapObject.getName();
      RouteProfiles.setCoordinates(name, centeredPosition);
    }
  }
}
