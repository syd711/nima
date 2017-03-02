package com.starsailor.editor.ui;

import com.starsailor.model.GameData;

/**
 *
 */
public class ShipItemFormPane extends FormPane {

  public ShipItemFormPane(MainPane mainPane) {
    super(mainPane);
  }

  @Override
  public void setData(GameData gameData) throws Exception {
    super.setData(gameData);

    if(gameData != null) {
      createSection(gameData, "Ship Data", gameData.getParent() != null, null);
    }
  }
}
