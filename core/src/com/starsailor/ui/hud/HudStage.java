package com.starsailor.ui.hud;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.starsailor.Game;
import com.starsailor.actors.Selectable;

/**
 *
 */
public class HudStage extends Stage {
  public final InfoTable headerTable;
  public final ActionPanel actionPanel;
  public final WeaponPanel weaponPanel;

  public HudStage() {
    headerTable = new InfoTable();
    actionPanel = new ActionPanel(this);
    weaponPanel = new WeaponPanel(this);
    addActor(headerTable);
    addActor(actionPanel);
    addActor(weaponPanel);

    Game.inputManager.addInputProcessor(this);
  }

  public void activateActionPanel(Selectable entity) {
    actionPanel.activate(entity);
  }

  public void deactivateActionPanel() {
    weaponPanel.deactivate();
    actionPanel.deactivate();
  }
}
