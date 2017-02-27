package com.starsailor.editor;

import com.starsailor.data.*;
import com.starsailor.editor.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class UIController {
  private static UIController instance = new UIController();

  private GameDataLoader loader;

  private UIController() {
    loader = new GameDataLoader();
  }

  public static UIController getInstance() {
    return instance;
  }

  public List<GameDataWithId> getShields() {
    List<GameDataWithId> result = new ArrayList<>();
    loader.collectModels(loader.getShieldsTreeModel(), result);
    return result;
  }

  public GameDataWithId getModel(int id) {
    List<GameDataWithId> allModels = loader.getAllModels();
    for(GameDataWithId model : allModels) {
      if(model.getId() == id) {
        return model;
      }
    }
    return null;
  }


  public GameData newChildFor(GameData parent) {
    GameData child = null;
    if(parent instanceof ShipData) {
      child = newShipData(parent);
    }
    else if(parent instanceof ShieldData) {
      child = newShieldData(parent);
    }

    parent.getChildren().add(child);
    return child;
  }

  //----------------------- Helper ----------------------------------------------------------

  private ShieldData newShieldData(GameData parent) {
    ShieldData shieldData = new ShieldData(IdGenerator.getInstance().createId(), (ShieldData) parent);
    shieldData.setName("New Shield (" + shieldData.getId() + ")");
    shieldData.setExtendParentData(false); //not extendable
    return shieldData;
  }

  private ShipData newShipData(GameData parent) {
    ShipData shipData = new ShipData(IdGenerator.getInstance().createId());
    shipData.setParent((ShipData) parent);
    shipData.setName("New Ship (" + shipData.getId() + ")");
    shipData.setBodyData(null);
    shipData.setSteeringData(null);
    shipData.setSpineData(null);
    return shipData;
  }

  public GameDataLoader getGameDataLoader() {
    return loader;
  }

  public void save() {
    loader.save();
  }
}
