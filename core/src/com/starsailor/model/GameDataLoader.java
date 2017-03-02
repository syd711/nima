package com.starsailor.model;

import com.starsailor.model.items.ShipItem;

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
  public static final String SHIP_ITEMS = "shipItems";

  private ShipData shipsRoot;
  private com.starsailor.model.ShieldData shieldsRoot;
  private WeaponData weaponsData;
  private ShipItem shipItemRoot;


  public GameDataLoader() {
  }



  public List<GameDataWithId> getGameDataFor(List<Integer> ids) {
    List<GameDataWithId> result = new ArrayList<>();
    for(Integer id : ids) {
      GameDataWithId item = getModel(id);
      if(item != null) {
        result.add(item);
      }
    }
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

  //TODO
  public void load() {
    //resolve tree
    List<GameDataWithId> allModels = getAllModels();
    for(GameDataWithId model : allModels) {
      if(model instanceof WeaponData) {

      }
      else if(model instanceof ShipData) {

      }
      else if(model instanceof com.starsailor.model.ShieldData) {

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

  public com.starsailor.model.ShieldData getShieldsTreeModel() {
    if(shieldsRoot == null) {
      shieldsRoot = load(SHIELDS, com.starsailor.model.ShieldData.class);
    }

    return shieldsRoot;
  }

  public ShipItem getShipItemsTreeModel() {
    if(shipItemRoot == null) {
      shipItemRoot = load(SHIP_ITEMS, ShipItem.class);
    }

    return shipItemRoot;
  }

  public void save() {
    File folder = new File(ASSETS_DATA);
    File file = new File(folder, SHIPS + ".json");
    if(file.exists()) {
      file.delete();
    }
    com.starsailor.model.JsonDataFactory.saveDataEntity(file, getShipsTreeModel());

    file = new File(folder, SHIELDS + ".json");
    if(file.exists()) {
      file.delete();
    }
    com.starsailor.model.JsonDataFactory.saveDataEntity(file, getShieldsTreeModel());

    file = new File(folder, WEAPONS + ".json");
    if(file.exists()) {
      file.delete();
    }
    com.starsailor.model.JsonDataFactory.saveDataEntity(file, getWeaponsTreeModel());

    file = new File(folder, SHIP_ITEMS + ".json");
    if(file.exists()) {
      file.delete();
    }
    com.starsailor.model.JsonDataFactory.saveDataEntity(file, getShipItemsTreeModel());
  }

  //---------------------- Helper ------------------------------------------------

  public void collectModels(GameDataWithId gameData, List result) {
    if(gameData.getParent() != null) {
      result.add(gameData);
    }
    if(gameData.getChildren() != null) {
      for(Object child : gameData.getChildren()) {
        collectModels((GameDataWithId) child, result);
      }
    }
  }

  private <T> T load(String name, Class<T> entity) {
    File file = new File(ASSETS_DATA + name + ".json");
    return com.starsailor.model.JsonDataFactory.loadDataEntity(file, entity);
  }

  public List<WeaponData> getWeapons() {
   List<WeaponData> result = new ArrayList<>();
   collectModels(getWeaponsTreeModel(), result);
   return result;
  }
}
