package com.starsailor.editor;

import com.starsailor.data.GameData;
import com.starsailor.data.GameDataLoader;
import com.starsailor.data.ShipData;

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
}
