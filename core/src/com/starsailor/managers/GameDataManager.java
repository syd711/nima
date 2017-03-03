package com.starsailor.managers;

import com.starsailor.model.*;
import com.starsailor.model.items.ShipItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to load the json data model
 */
public class GameDataManager {
  public static String ASSETS_FOLDER = "./";

  public static final String SHIPS = "ships";
  public static final String WEAPONS = "weapons";
  public static final String SHIELDS = "shields";
  public static final String SHIP_ITEMS = "shipItems";

  private ShipData shipsRoot;
  private ShieldData shieldsRoot;
  private WeaponData weaponsData;
  private ShipItem shipItemRoot;

  private static GameDataManager instance = new GameDataManager();

  private GameDataManager() {
  }

  public static GameDataManager getInstance() {
    return instance;
  }

  public void loadMapObjects() {
//    File[] files = getMapsFolder().listFiles(new FilenameFilter() {
//      @Override
//      public boolean accept(File dir, String name) {
//        return name.endsWith(".tmx");
//      }
//    });
//
//    try {
//      for(File file : files) {
//        TiledMap map = new TmxMapLoader().load(file.getAbsolutePath());
//        Iterator<MapLayer> iterator = map.getLayers().iterator();
//        while(iterator.hasNext()) {
//          MapLayer layer = iterator.next();
//          Iterator<MapObject> objectIterator = layer.getObjects().iterator();
//          while(objectIterator.hasNext()) {
//            MapObject mapObject = objectIterator.next();
//
//          }
//        }
//      }
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
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
    loadMapObjects();
    load(getWeaponsTreeModel());
    load(getShieldsTreeModel());
    load(getShipsTreeModel());
    load(getShipItemsTreeModel());

    //resolve tree
    List<GameDataWithId> allModels = getAllModels();
    for(GameDataWithId model : allModels) {
      if(model instanceof WeaponData) {

      }
      else if(model instanceof ShipData) {
        ShipData shipData = (ShipData) model;
        List<Integer> weapons = shipData.getStatusData().getWeapons();
        for(Integer weapon : weapons) {
          WeaponData weaponData = (WeaponData) getModel(weapon);
          shipData.getStatusData().getWeaponDatas().add(weaponData);
        }

        int shieldId = shipData.getStatusData().getShield();
        if(shieldId > 0) {
          ShieldData shieldData = (ShieldData) getModel(shieldId);
          shipData.getStatusData().setShieldData(shieldData);
        }
      }
      else if(model instanceof ShieldData) {
        ShieldData shieldData = (ShieldData) model;
      }
      else if(model instanceof ShipItem) {
        ShipItem shipItem = (ShipItem) model;
        shipItem.setShipData((ShipData) getModel(shipItem.getShipType()));
      }
    }
  }


  public List<GameDataWithId> getAllModels() {
    List<GameDataWithId> result = new ArrayList<>();
    collectModels(getShieldsTreeModel(), result);
    collectModels(getWeaponsTreeModel(), result);
    collectModels(getShipsTreeModel(), result);
    collectModels(getShipItemsTreeModel(), result);
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

  //---------------------- Helper ------------------------------------------------

  private File getMapsFolder() {
    return new File(ASSETS_FOLDER + "maps/main/");
  }

  private File getDataFolder() {
    return new File(ASSETS_FOLDER + "data/");
  }


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

  private void load(GameData gameData) {
    List<GameData> children = gameData.getChildren();
    if(children != null) {
      for(GameData child : children) {
        child.setParent(gameData);
        load(child);
      }
    }
  }

  private <T> T load(String name, Class<T> entity) {
    File file = new File(getDataFolder(),name + ".json");
    return com.starsailor.model.JsonDataFactory.loadDataEntity(file, entity);
  }

  public List<WeaponData> getWeapons() {
   List<WeaponData> result = new ArrayList<>();
   collectModels(getWeaponsTreeModel(), result);
   return result;
  }
}
