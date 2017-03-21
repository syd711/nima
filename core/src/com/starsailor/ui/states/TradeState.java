package com.starsailor.ui.states;

import com.starsailor.actors.FormationOwner;
import com.starsailor.actors.Selectable;
import com.starsailor.actors.Ship;
import com.starsailor.actors.states.formation.FormationTradingState;
import com.starsailor.actors.states.npc.NPCStates;
import com.starsailor.managers.SelectionManager;
import com.starsailor.ui.stages.GameStage;

/**
 * Entered when the user selects a ship
 */
public class TradeState extends UIState {

  @Override
  public void enter(GameStage entity) {
    Selectable selection = SelectionManager.getInstance().getSelection();
    Ship ship = (Ship) selection;

    ((FormationOwner)ship.getFormationOwner()).changeState(new FormationTradingState());
    ship.changeState(NPCStates.TRADING_STATE);
  }

  @Override
  public void update(GameStage entity) {
  }

  @Override
  public void exit(GameStage entity) {

  }
}
