package com.starsailor.editor;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.starsailor.editor.util.IdGenerator;
import com.starsailor.managers.GameDataManager;
import com.starsailor.model.*;
import com.starsailor.model.items.MapItem;
import com.starsailor.model.items.ShipItem;

import java.io.File;
import java.util.*;

/**
 *
 */
public class UIController {
  public static String TMX_FILE_FOLDER = "../../core/assets/maps/main/";
  private static UIController instance = new UIController();

  private GameDataManager dataManager;

  private Map<File,TiledMap> tmxMaps = new HashMap<>();

  private UIController() {
    dataManager = GameDataManager.getInstance();

//    File[] files = new File(TMX_FILE_FOLDER).listFiles(new FilenameFilter() {
//      @Override
//      public boolean accept(File dir, String name) {
//        return name.endsWith(".tmx");
//      }
//    });
//
//    try {
//      for(File file : files) {
//        FileHandleResolver resolver = new ExternalFileHandleResolver();
//        TiledMap map = new TmxMapLoader(resolver).load(file.getAbsolutePath());
//        tmxMaps.put(file, map);
//      }
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
  }

  public static UIController getInstance() {
    return instance;
  }

  public File getTmxFileFor(MapItem gameData) {
    for(Map.Entry<File, TiledMap> maps : tmxMaps.entrySet()) {
      TiledMap map = maps.getValue();
      Iterator<MapLayer> iterator = map.getLayers().iterator();
      while(iterator.hasNext()) {
        MapLayer layer = iterator.next();
        Iterator<MapObject> objectIterator = layer.getObjects().iterator();
        while(objectIterator.hasNext()) {
          MapObject mapObject = objectIterator.next();
          if(mapObject.getName().toLowerCase().startsWith(gameData.getName().toLowerCase())) {
            return maps.getKey();
          }
          if(mapObject.getName().toLowerCase().startsWith(String.valueOf(gameData.getId()))) {
            return maps.getKey();
          }
        }
      }
    }
    return null;
  }

  public List<GameDataWithId> getShields() {
    List<GameDataWithId> result = new ArrayList<>();
    dataManager.collectModels(dataManager.getShieldsTreeModel(), result);
    return result;
  }

  public List<GameDataWithId> getShips() {
    List<GameDataWithId> result = new ArrayList<>();
    dataManager.collectModels(dataManager.getShipsTreeModel(), result);
    return result;
  }

  public List<GameDataWithId> getShipItems() {
    List<GameDataWithId> result = new ArrayList<>();
    dataManager.collectModels(dataManager.getShipItemsTreeModel(), result);
    return result;
  }

  public GameData newChildFor(GameData parent) {
    GameData child = null;
    if(parent instanceof ShipData) {
      child = newShipData(parent);
    }
    else if(parent instanceof ShieldData) {
      child = newShieldData(parent);
    }
    else if(parent instanceof ShipItem) {
      child = newDataEntity(parent);
    }
    else if(parent instanceof WeaponData) {
      WeaponData weaponData = (WeaponData) parent;
      WeaponData.Types type = WeaponData.Types.valueOf(weaponData.getType().toUpperCase());

      switch(type) {
        case LASER: {
          child = newLaserWeaponData(weaponData);
          break;
        }
        case MISSILE: {
          child = newMissileWeaponData(weaponData);
          break;
        }
        case PHASER: {
          child = newPhaserWeaponData(weaponData);
          break;
        }
        case MINE: {
          child = newMineWeaponData(weaponData);
          break;
        }
        case FLARES: {
          child = newFlaresWeaponData(weaponData);
          break;
        }
      }
    }

    if(parent.getChildren() == null) {
      parent.setChildren(new ArrayList());
    }

    parent.getChildren().add(child);
    return child;
  }

  public GameDataManager getGameDataLoader() {
    return dataManager;
  }

  public void save() {
    dataManager.save();
  }

  //----------------------- Helper ----------------------------------------------------------

  private ShieldData newShieldData(GameData parent) {
    ShieldData shieldData = new ShieldData(IdGenerator.getInstance().createId(), (ShieldData) parent);
    shieldData.setName("New Shield (" + shieldData.getId() + ")");
    shieldData.setExtendParentData(false); //not extendable
    return shieldData;
  }

  private ShipItem newDataEntity(GameData parent) {
    ShipItem entity = new ShipItem(IdGenerator.getInstance().createId(), (ShipItem) parent);
    entity.setParent((ShipItem) parent);
    entity.setExtendParentData(true);
    entity.setFraction(((ShipItem) parent).getFraction());
    entity.setShipType(((ShipItem) parent).getShipType());
    return entity;
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

  //-- Weapons --

  private GameData newFlaresWeaponData(WeaponData parent) {
    WeaponData child = new WeaponData(IdGenerator.getInstance().createId(), WeaponData.Types.FLARES);
    child.setBodyData(new BodyData(parent.getBodyData()));
    child.setSteeringData(new SteeringData(parent.getSteeringData()));
    child.setTorque(parent.getTorque());

    applyCommonWeaponData(parent, child);
    return child;
  }

  private GameData newMineWeaponData(WeaponData parent) {
    WeaponData child = new WeaponData(IdGenerator.getInstance().createId(), WeaponData.Types.MINE);
    child.setBodyData(new BodyData(parent.getBodyData()));
    child.setActivationDistance(parent.getActivationDistance());

    applyCommonWeaponData(parent, child);
    return child;
  }

  private GameData newPhaserWeaponData(WeaponData parent) {
    WeaponData child = new WeaponData(IdGenerator.getInstance().createId(), WeaponData.Types.PHASER);

    applyCommonWeaponData(parent, child);
    return child;
  }

  private GameData newMissileWeaponData(WeaponData parent) {
    WeaponData child = new WeaponData(IdGenerator.getInstance().createId(), WeaponData.Types.MISSILE);
    child.setBodyData(new BodyData(parent.getBodyData()));
    child.setSteeringData(new SteeringData(parent.getSteeringData()));
    child.setActivationDistance(parent.getActivationDistance());

    applyCommonWeaponData(parent, child);
    return child;
  }

  private GameData newLaserWeaponData(WeaponData parent) {
    WeaponData child = new WeaponData(IdGenerator.getInstance().createId(), WeaponData.Types.LASER);
    child.setBodyData(new BodyData(parent.getBodyData()));

    applyCommonWeaponData(parent, child);
    return child;
  }

  private void applyCommonWeaponData(WeaponData parent, WeaponData child) {
    child.setParent(parent);

    child.setName(parent.getName() + " (" + child.getId() + ")");
    child.setDamage(parent.getDamage());
    child.setSprite(parent.getSprite());
    child.setCategory(parent.getCategory());
    child.setSound(parent.getSound());
    child.setBulletCount(parent.getBulletCount());
    child.setForceFactor(parent.getForceFactor());
    child.setRechargeTimeMillis(parent.getRechargeTimeMillis());
    child.setCollisionEffect(parent.getCollisionEffect());
    child.setImpactFactor(parent.getImpactFactor());
  }

  public void removeDuplicates(List<GameDataWithId> allEntities, List<GameDataWithId> selection) {
    for(GameDataWithId weapon : selection) {
      if(allEntities.contains(weapon)) {
        allEntities.remove(weapon);
      }
    }
  }
}
