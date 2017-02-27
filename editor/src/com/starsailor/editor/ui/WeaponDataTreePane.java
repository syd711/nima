package com.starsailor.editor.ui;

import com.starsailor.data.GameDataWithId;
import com.starsailor.editor.UIController;

/**
 *
 */
public class WeaponDataTreePane<WeaponData> extends GameDataTreePane {

  public WeaponDataTreePane(MainPane mainPane) {
    super(mainPane, "Weapons");
  }

  @Override
  protected GameDataWithId<com.starsailor.data.WeaponData> getRoot() {
    return UIController.getInstance().getGameDataLoader().getWeaponsTreeModel();
  }

  @Override
  protected boolean isRootVisible() {
    return false;
  }
}
