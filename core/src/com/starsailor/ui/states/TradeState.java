package com.starsailor.ui.states;

import com.starsailor.actors.FormationOwner;
import com.starsailor.actors.Player;
import com.starsailor.actors.Selectable;
import com.starsailor.actors.Ship;
import com.starsailor.actors.states.formation.FormationRouteState;
import com.starsailor.actors.states.npc.NPCStates;
import com.starsailor.actors.states.player.PlayerStates;
import com.starsailor.managers.SelectionManager;
import com.starsailor.ui.stages.GameStage;

/**
 * Entered when the user selects a ship
 */
public class TradeState extends UIState {

  private Ship tradingShip;

  @Override
  public void enter(GameStage entity) {
    Selectable selection = SelectionManager.getInstance().getSelection();
    tradingShip = (Ship) selection;

    tradingShip.changeState(NPCStates.TRADING_STATE);
    Player.getInstance().changeState(PlayerStates.TRADING);
  }

  @Override
  public void update(GameStage entity) {
  }

  @Override
  public void exit(GameStage entity) {
    tradingShip.changeState(tradingShip.statefulComponent.stateMachine.getPreviousState());
    FormationOwner formationOwner = (FormationOwner) tradingShip.getFormationOwner();
    if(formationOwner != null) {
      ((FormationOwner)tradingShip.getFormationOwner()).changeState(new FormationRouteState());
    }
  }
}
