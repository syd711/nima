package com.starsailor.editor;

import com.starsailor.data.*;
import com.starsailor.editor.util.ShipDataSerializer;

import java.io.File;

/**
 *
 */
public class UIController {
  private static UIController instance = new UIController();

  private UIController() {
  }

  public static UIController getInstance() {
    return instance;
  }

  public GameData getTreeModel() {
    GameData root = new ShipData();
    GameDataLoader loader = new GameDataLoader(new File("../../core/assets/ships/ships.json"));
    ShipData ships = loader.load(ShipData.class);
    root.addChild(ships);
    return root;
  }

  public ShipData newChildFor(ShipData parent) {
    ShipData shipData = new ShipData();
    shipData.setName("New Ship");

    BodyData bodyData = new BodyData();
    bodyData.setExtendParentData(true);
    shipData.setBodyData(bodyData);

    SteeringData steeringData = new SteeringData();
    steeringData.setExtendParentData(true);
    shipData.setSteeringData(steeringData);

    parent.getChildren().add(shipData);
    return shipData;
  }

  public void save(GameData root) {
    File file = new File("g:/temp/ships.json");
    if(file.exists()) {
      file.delete();
    }
    JsonDataFactory.saveDataEntity(file, root, new ShipDataSerializer());
  }
}
