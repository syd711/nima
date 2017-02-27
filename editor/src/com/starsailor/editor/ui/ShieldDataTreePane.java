package com.starsailor.editor.ui;

import com.starsailor.data.GameDataWithId;
import com.starsailor.editor.UIController;

/**
 *
 */
public class ShieldDataTreePane<ShieldData> extends GameDataTreePane {
  public ShieldDataTreePane(MainPane mainPane) {
    super(mainPane, "Shields");
  }

  @Override
  protected GameDataWithId getRoot() {
    return UIController.getInstance().getGameDataLoader().getShieldsTreeModel();
  }
}
