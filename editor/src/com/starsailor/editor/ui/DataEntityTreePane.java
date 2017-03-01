package com.starsailor.editor.ui;

import com.starsailor.data.GameDataWithId;
import com.starsailor.editor.UIController;

/**
 *
 */
public class DataEntityTreePane<DataEntity> extends GameDataTreePane {
  public DataEntityTreePane(MainPane mainPane) {
    super(mainPane, "Data Entity");
  }

  @Override
  protected GameDataWithId getRoot() {
    return UIController.getInstance().getGameDataLoader().getDataEntityTreeModel();
  }

  @Override
  protected boolean isRootVisible() {
    return true;
  }
}
