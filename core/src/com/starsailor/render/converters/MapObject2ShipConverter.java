package com.starsailor.render.converters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.NPC;
import com.starsailor.actors.NPCLauncher;
import com.starsailor.actors.ShipFactory;
import com.starsailor.GameDataManager;
import com.starsailor.model.GameDataWithId;
import com.starsailor.model.items.ShipItem;
import com.starsailor.render.TiledMapFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Initializes the map positions of route stations.
 */
public class MapObject2ShipConverter extends DefaultMapObjectConverter {

  private boolean enabled;
  private List<NPC> newEntities = new ArrayList<>();

  public MapObject2ShipConverter(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public boolean isApplicable(TiledMapFragment mapFragment, MapObject mapObject) {
    String possibleId = mapObject.getName();
    try {
      Integer.parseInt(possibleId);
      return true;
    }
    catch (NumberFormatException e) {
      return false;
    }
  }

  @Override
  public void convertMapObject(TiledMapFragment mapFragment, MapObject mapObject) {
    if(!enabled) {
      return;
    }

    int mapItemId = Integer.parseInt(mapObject.getName());
    GameDataWithId model = GameDataManager.getInstance().getModel(mapItemId);
    if(model != null) {
      if(model instanceof ShipItem) {
        ShipItem shipItem = (ShipItem) model;
        Vector2 centeredPosition = (Vector2) mapObject.getProperties().get(MapConstants.PROPERTY_CENTERED_POSITION);

        NPC npc = ShipFactory.createNPC(shipItem, centeredPosition);
        newEntities.add(npc);
        Gdx.app.log(this.getClass().getName(), "Created '" + npc + "'");
      }
    }
  }

  @Override
  public void finalizeConverter() {
    NPCLauncher.launch(new ArrayList<>(newEntities));
    newEntities.clear();
  }
}
