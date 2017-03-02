package com.starsailor.editor.ui;

import com.starsailor.model.GameDataWithId;
import com.starsailor.editor.UIController;

/**
 *
 */
public class WeaponDataTreePane<WeaponData> extends GameDataTreePane {

  public WeaponDataTreePane(MainPane mainPane) {
    super(mainPane, "Weapons");
  }

  @Override
  protected GameDataWithId<com.starsailor.model.WeaponData> getRoot() {
    return UIController.getInstance().getGameDataLoader().getWeaponsTreeModel();
  }

  @Override
  protected boolean isRootVisible() {
    return false;
  }
}
