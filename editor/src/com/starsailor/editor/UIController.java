package com.starsailor.editor;

import com.starsailor.editor.util.IdGenerator;
import com.starsailor.model.*;
import com.starsailor.model.items.MapItem;
import com.starsailor.model.items.ShipItem;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class UIController {
  public static String TMX_FILE_FOLDER = "../../core/assets/maps/main/";
  private static UIController instance = new UIController();

  private GameDataLoader loader;

  private Map<File,String> tmxMaps = new HashMap<>();

  private UIController() {
    loader = new GameDataLoader();

    File[] files = new File(TMX_FILE_FOLDER).listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(".tmx");
      }
    });

    try {
      for(File file : files) {
        byte[] encoded = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        String result = new String(encoded, "utf-8");
        tmxMaps.put(file, result);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static UIController getInstance() {
    return instance;
  }

  public File getTmxFileFor(MapItem gameData) {
    for(Map.Entry<File, String> maps : tmxMaps.entrySet()) {
      if(maps.getValue().contains("name=\"" + gameData.getId() + "\"")) {
        return maps.getKey();
      }
    }
    return null;
  }

  public List<GameDataWithId> getShields() {
    List<GameDataWithId> result = new ArrayList<>();
    loader.collectModels(loader.getShieldsTreeModel(), result);
    return result;
  }

  public List<GameDataWithId> getShips() {
    List<GameDataWithId> result = new ArrayList<>();
    loader.collectModels(loader.getShipsTreeModel(), result);
    return result;
  }

  public List<GameDataWithId> getShipItems() {
    List<GameDataWithId> result = new ArrayList<>();
    loader.collectModels(loader.getShipItemsTreeModel(), result);
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

  public GameDataLoader getGameDataLoader() {
    return loader;
  }

  public void save() {
    loader.save();
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
