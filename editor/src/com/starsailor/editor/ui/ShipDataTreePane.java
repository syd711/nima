package com.starsailor.editor.ui;

import com.starsailor.data.GameDataWithId;
import com.starsailor.editor.UIController;

/**
 *
 */
public class ShipDataTreePane<ShipData> extends GameDataTreePane {

  public ShipDataTreePane(MainPane mainPane) {
    super(mainPane, "Ships");
  }

  @Override
  protected GameDataWithId<com.starsailor.data.ShipData> getRoot() {
    return UIController.getInstance().getGameDataLoader().getShipsTreeModel();
  }
}
