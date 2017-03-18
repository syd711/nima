package com.starsailor.ui.states;

import com.starsailor.GameStateManager;
import com.starsailor.ui.UIManager;
import com.starsailor.ui.stages.GameStage;
import com.starsailor.ui.stages.hud.HudStage;

/**
 *
 */
public class NavigationSelectionState extends UIState {
  private HudStage hudStage;

  public NavigationSelectionState() {
    hudStage = UIManager.getInstance().getHudStage();
  }

  @Override
  public void enter(GameStage entity) {
    GameStateManager.getInstance().setPaused(true);
    hudStage.getNavigationPanel().activate();
  }

  @Override
  public void update(GameStage entity) {

  }

  @Override
  public void exit(GameStage entity) {
    GameStateManager.getInstance().setPaused(false);
    hudStage.getNavigationPanel().deactivate();
  }
}
