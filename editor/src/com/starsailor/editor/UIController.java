package com.starsailor.editor;

import com.starsailor.data.*;
import com.starsailor.editor.util.GameDataSerializer;
import com.starsailor.editor.util.IdGenerator;

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

  public ShipData getTreeModel() {
    GameDataLoader loader = new GameDataLoader(new File("G:/temp/ships.json"));
    ShipData root = loader.load(ShipData.class);
    return root;
  }

  public ShipData newChildFor(ShipData parent) {
    ShipData shipData = new ShipData(IdGenerator.getInstance().createId());
    shipData.setParent(parent);
    shipData.setName("New Ship (" + shipData.getId() + ")");

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
    JsonDataFactory.saveDataEntity(file, root, GameData.class, new GameDataSerializer());
  }
}
