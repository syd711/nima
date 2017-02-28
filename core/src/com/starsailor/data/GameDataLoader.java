package com.starsailor.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to load the json data model
 */
public class GameDataLoader {
  public static String ASSETS_DATA = "./assets/data/";

  public static final String SHIPS = "ships";
  public static final String WEAPONS = "weapons";
  public static final String SHIELDS = "shields";

  private ShipData shipsRoot;
  private ShieldData shieldsRoot;
  private WeaponData weaponsData;


  public GameDataLoader() {
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

  public void load() {
    //resolve tree
    List<GameDataWithId> allModels = getAllModels();
    for(GameDataWithId model : allModels) {
      if(model instanceof WeaponData) {

      }
      else if(model instanceof ShipData) {

      }
      else if(model instanceof ShieldData) {

      }
    }
  }


  public List<GameDataWithId> getAllModels() {
    List<GameDataWithId> result = new ArrayList<>();
    collectModels(getShieldsTreeModel(), result);
    collectModels(getWeaponsTreeModel(), result);
    collectModels(getShipsTreeModel(), result);
    return result;
  }

  public ShipData getShipsTreeModel() {
    if(shipsRoot == null) {
      shipsRoot = load(SHIPS, ShipData.class);
    }

    return shipsRoot;
  }

  public WeaponData getWeaponsTreeModel() {
    if(weaponsData == null) {
      weaponsData = load(WEAPONS, WeaponData.class);
    }

    return weaponsData;
  }

  public ShieldData getShieldsTreeModel() {
    if(shieldsRoot == null) {
      shieldsRoot = load(SHIELDS, ShieldData.class);
    }

    return shieldsRoot;
  }


  public void save() {
    File folder = new File(ASSETS_DATA);
    File file = new File(folder, SHIPS + ".json");
    if(file.exists()) {
      file.delete();
    }
    JsonDataFactory.saveDataEntity(file, getShipsTreeModel());

    file = new File(folder, SHIELDS + ".json");
    if(file.exists()) {
      file.delete();
    }
    JsonDataFactory.saveDataEntity(file, getShieldsTreeModel());

    file = new File(folder, WEAPONS + ".json");
    if(file.exists()) {
      file.delete();
    }
    JsonDataFactory.saveDataEntity(file, getWeaponsTreeModel());
  }

  //---------------------- Helper ------------------------------------------------

  public void collectModels(GameDataWithId gameData, List<GameDataWithId> result) {
    result.add(gameData);
    for(Object child : gameData.getChildren()) {
      collectModels((GameDataWithId) child, result);
    }
  }


  private <T> T load(String name, Class<T> entity) {
    File file = new File(ASSETS_DATA + name + ".json");
    return JsonDataFactory.loadDataEntity(file, entity);
  }
}
