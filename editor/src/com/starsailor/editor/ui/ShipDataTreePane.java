package com.starsailor.editor.ui;

import com.starsailor.model.GameDataWithId;
import com.starsailor.editor.UIController;

/**
 *
 */
public class ShipDataTreePane<ShipData> extends GameDataTreePane {

  public ShipDataTreePane(MainPane mainPane) {
    super(mainPane, "Ship Types");
  }

  @Override
  protected GameDataWithId<com.starsailor.model.ShipData> getRoot() {
    return UIController.getInstance().getGameDataLoader().getShipsTreeModel();
  }
}
