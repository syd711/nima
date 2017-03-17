package com.starsailor.ui.states;

import com.starsailor.managers.UIManager;
import com.starsailor.ui.stages.GameStage;
import com.starsailor.ui.stages.hud.HudStage;
import com.starsailor.ui.stages.hud.weapons.WeaponsPanel;

/**
 * Entered when the user selects a ship
 */
public class BattleState extends UIState {

  private final WeaponsPanel weaponsPanel;

  public BattleState() {
    HudStage hudStage = UIManager.getInstance().getHudStage();
    weaponsPanel = hudStage.getWeaponsPanel();
  }

  @Override
  public void enter(GameStage entity) {
    weaponsPanel.activate();
  }

  @Override
  public void update(GameStage entity) {
  }

  @Override
  public void exit(GameStage entity) {
    weaponsPanel.deactivate();
  }
}
