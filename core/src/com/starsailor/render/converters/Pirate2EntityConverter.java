package com.starsailor.render.converters;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.NPC;
import com.starsailor.actors.NPCFactory;
import com.starsailor.actors.Route;
import com.starsailor.actors.states.npc.NPCStates;
import com.starsailor.components.RouteComponent;
import com.starsailor.data.DataEntities;
import com.starsailor.managers.EntityManager;
import com.starsailor.render.TiledMapFragment;

import static com.starsailor.render.converters.MapConstants.PROPERTY_OBJECT_TYPE;

/**
 * Initializes the map positions of route stations.
 */
public class Pirate2EntityConverter extends DefaultMapObjectConverter {

  private boolean enabled;

  public Pirate2EntityConverter(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public boolean isApplicable(TiledMapFragment mapFragment, MapObject mapObject) {
    String type = (String) mapObject.getProperties().get(PROPERTY_OBJECT_TYPE);
    return type != null && type.equalsIgnoreCase(MapConstants.TYPE_PIRATE);
  }

  @Override
  public void convertMapObject(TiledMapFragment mapFragment, MapObject mapObject) {
    if(!enabled) {
      return;
    }

    Vector2 centeredPosition = (Vector2) mapObject.getProperties().get(MapConstants.PROPERTY_CENTERED_POSITION);
    String shipProfile = (String) mapObject.getProperties().get(MapConstants.PROPERTY_SHIP_PROFILE);
    String defaultStateName = (String) mapObject.getProperties().get(MapConstants.PROPERTY_STATE);

    if(shipProfile == null) {
      throw new UnsupportedOperationException("No shipProfile found for data entity '" + mapObject.getName() + "'");
    }

    NPC npc = NPCFactory.createPirate(DataEntities.getShip(shipProfile), NPCStates.forName(defaultStateName), centeredPosition);
    Gdx.app.log(this.getClass().getName(), "Added pirate ship '" + npc + "'");
  }

  /**
   * Checks if a route entity exists for the given name or creates a new one
   */
  private Route getRoute(String name) {
    ImmutableArray<Entity> entitiesFor = EntityManager.getInstance().getEntitiesFor(RouteComponent.class);
    for(Entity entity : entitiesFor) {
      if(entity instanceof Route) {
        Route route = (Route) entity;
        if(route.getName().equalsIgnoreCase(name)) {
          return route;
        }
      }
    }
    return null;
  }
}
