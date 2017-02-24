package com.starsailor.editor;

import com.starsailor.data.*;

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
    GameData root = new GameData();
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
    return shipData;
  }
}
