package com.starsailor.editor.ui;

import com.starsailor.data.GameData;

/**
 *
 */
public class ShieldDataFormPane extends FormPane {

  public ShieldDataFormPane(MainPane mainPane) {
    super(mainPane);
  }

  @Override
  public void setData(GameData gameData) throws Exception {
    super.setData(gameData);

    if(gameData != null) {
      createSection(gameData, "Shield Data", false, this);
    }

  }
}
