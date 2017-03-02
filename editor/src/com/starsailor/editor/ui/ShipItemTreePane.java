package com.starsailor.editor.ui;

import com.starsailor.model.GameDataWithId;
import com.starsailor.editor.UIController;

/**
 *
 */
public class ShipItemTreePane extends GameDataTreePane {
  public ShipItemTreePane(MainPane mainPane) {
    super(mainPane, "Ships");
  }

  @Override
  protected GameDataWithId getRoot() {
    return UIController.getInstance().getGameDataLoader().getShipItemsTreeModel();
  }

  @Override
  protected boolean isRootVisible() {
    return true;
  }
}
