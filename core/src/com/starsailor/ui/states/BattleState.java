package com.starsailor.ui.states;

import com.starsailor.actors.Player;
import com.starsailor.ui.stages.GameStage;

/**
 * Entered when the user selects a ship
 */
public class BattleState extends UIState {

  @Override
  public void enter(GameStage entity) {
    Player.getInstance().switchToBattleState(null);
  }

  @Override
  public void exit(GameStage entity) {
    Player.getInstance().switchToDefaultState();
  }
}
