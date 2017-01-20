package com.starsailor.ui.hud;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.starsailor.actors.GameEntity;

/**
 *
 */
public class HudStage extends Stage {
  private final InfoTable headerTable;
  private final ActionPanel actionTable;

  public HudStage() {
    headerTable = new InfoTable();
    actionTable = new ActionPanel();
    addActor(headerTable);
    addActor(actionTable);
  }

  public void activateActionPanel(GameEntity entity) {
    actionTable.activate(entity);
  }

  public void deactivateActionPanel() {
    actionTable.deactivate();
  }
}
