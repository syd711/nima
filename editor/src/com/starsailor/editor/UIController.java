package com.starsailor.editor;

import com.starsailor.data.*;
import com.starsailor.editor.util.IdGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class UIController {
  private static UIController instance = new UIController();

  private ShipData shipsRoot;
  private ShieldData shieldsRoot;
  private List<GameData> shields;

  private UIController() {
  }

  public static UIController getInstance() {
    return instance;
  }

  public ShipData getShipsTreeModel() {
    if(shipsRoot == null) {
      GameDataLoader loader = new GameDataLoader(new File("G:/temp/ships.json"));
      shipsRoot = loader.load(ShipData.class);
    }

    return shipsRoot;
  }

  public ShieldData getShieldsTreeModel() {
    if(shieldsRoot == null) {
      GameDataLoader loader = new GameDataLoader(new File("G:/temp/shields.json"));
      shieldsRoot = loader.load(ShieldData.class);
    }

    return shieldsRoot;
  }


  public List<GameDataWithId> getShields() {
    List<GameDataWithId> result = new ArrayList<>();
    collectModels(getShieldsTreeModel(), result);
    return result;
  }

  public GameDataWithId getModel(int id) {
    List<GameDataWithId> allModels = getAllModels();
    for(GameDataWithId model : allModels) {
      if(model.getId() == id) {
        return model;
      }
    }
    return null;
  }

  public List<GameDataWithId> getAllModels() {
    List<GameDataWithId> result = new ArrayList<>();
    collectModels(getShieldsTreeModel(), result);
    collectModels(getShipsTreeModel(), result);
    return result;
  }

  private void collectModels(GameDataWithId gameData, List<GameDataWithId> result) {
    result.add(gameData);
    for(Object child : gameData.getChildren()) {
      collectModels((GameDataWithId) child, result);
    }
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

  public void save() {
    File file = new File("g:/temp/ships.json");
    if(file.exists()) {
      file.delete();
    }
    JsonDataFactory.saveDataEntity(file, getShipsTreeModel());

    file = new File("g:/temp/shields.json");
    if(file.exists()) {
      file.delete();
    }
    JsonDataFactory.saveDataEntity(file, getShieldsTreeModel());
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

}
