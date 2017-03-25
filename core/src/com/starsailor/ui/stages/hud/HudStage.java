package com.starsailor.ui.stages.hud;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.starsailor.managers.InputManager;

/**
 * The general game stage for menus and side panels.
 */
public class HudStage extends Stage {

  private final NavigationPanel navigationPanel;
  private final WeaponsPanel weaponsPanel;
  private final ContextMenu contextMenu;
  private final NavigatorPanel navigatorPanel;
  private final InventoryPanel inventoryPanel;
  private final TradingPlayerPanel tradingPlayerPanel;
  private final TradingNPCPanel tradingNPCPanel;
  private final SavePanel savePanel;
  private final LootPanel lootPanel;

  public HudStage() {
    InputManager.getInstance().addInputProcessor(this);

    //add panels and context menu
    weaponsPanel = new WeaponsPanel();
    navigationPanel = new NavigationPanel();
    contextMenu = new ContextMenu();
    navigatorPanel = new NavigatorPanel();
    inventoryPanel = new InventoryPanel();
    tradingPlayerPanel = new TradingPlayerPanel();
    tradingNPCPanel = new TradingNPCPanel();
    savePanel = new SavePanel();
    lootPanel = new LootPanel();

    addActor(weaponsPanel);
    addActor(navigationPanel);
    addActor(contextMenu);
    addActor(navigatorPanel);
    addActor(inventoryPanel);
    addActor(tradingPlayerPanel);
    addActor(tradingNPCPanel);
    addActor(savePanel);
    addActor(lootPanel);
  }

  public ContextMenu getContextMenu() {
    return contextMenu;
  }

  public WeaponsPanel getWeaponsPanel() {
    return weaponsPanel;
  }

  public NavigationPanel getNavigationPanel() {
    return navigationPanel;
  }

  public NavigatorPanel getNavigatorPanel() {
    return navigatorPanel;
  }

  public InventoryPanel getIventoryPanel() {
    return inventoryPanel;
  }

  public TradingPlayerPanel getTradingPlayerPanel() {
    return tradingPlayerPanel;
  }

  public TradingNPCPanel getTradingNpcPanel() {
    return tradingNPCPanel;
  }

  public SavePanel getSavePanel() {
    return savePanel;
  }

  public LootPanel getLootPanel() {
    return lootPanel;
  }

  public boolean isInBattleState() {
    return getWeaponsPanel().isActive();
  }
}
