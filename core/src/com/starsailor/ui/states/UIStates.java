package com.starsailor.ui.states;

/**
 * States for the UI
 */
public class UIStates {

  public static UIState DEFAULT_STATE = new DefaultState();
  public static UIState SHIP_SELECTION_STATE = new ShipSelectionState();
  public static UIState BATTLE_STATE = new BattleState();
}
