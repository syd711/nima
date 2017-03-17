package com.starsailor.ui.states;

import com.starsailor.managers.GameStateManager;
import com.starsailor.managers.UIManager;
import com.starsailor.ui.stages.GameStage;
import com.starsailor.ui.stages.hud.HudStage;

/**
 * Entered when the user selects a ship
 */
public class ShipSelectionState extends UIState {
  private HudStage hudStage;

  public ShipSelectionState() {
    hudStage = UIManager.getInstance().getHudStage();
  }

  @Override
  public void enter(GameStage entity) {
    GameStateManager.getInstance().setPaused(true);
    hudStage.getContextMenu().show();
  }

  @Override
  public void update(GameStage entity) {

  }

  @Override
  public void exit(GameStage entity) {
    GameStateManager.getInstance().setPaused(false);
    hudStage.getContextMenu().hide();
  }
}
