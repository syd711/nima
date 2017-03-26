package com.starsailor.ui.states;

import com.starsailor.GameStateManager;
import com.starsailor.ui.stages.GameStage;

/**
 *
 */
public class UIIdleState extends UIState {
  @Override
  public void enter(GameStage entity) {
//    HudStage hudStage = UIManager.getInstance().getHudStage();
//    hudStage.getContextMenu().hide();
//
    GameStateManager.getInstance().setPaused(false);
//    SelectionManager.getInstance().resetSelection();
//
//    Player.getInstance().switchToDefaultState();
//
//    UIManager.getInstance().getHudStage().getWeaponsPanel().deactivate();
//    UIManager.getInstance().getHudStage().getTradingPlayerPanel().deactivate();
//    UIManager.getInstance().getHudStage().getTradingNpcPanel().deactivate();
  }


}
