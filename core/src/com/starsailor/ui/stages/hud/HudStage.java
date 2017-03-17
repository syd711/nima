package com.starsailor.ui.stages.hud;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.starsailor.Game;

/**
 * The general game stage for menus and side panels.
 */
public class HudStage extends Stage {

  private final NavigationPanel navigationPanel;
  private final WeaponsPanel weaponsPanel;
  private final ContextMenu contextMenu;
  private final NavigatorPanel navigatorPanel;

  public HudStage() {
    Game.inputManager.addInputProcessor(this);

    //add panels and context menu
    weaponsPanel = new WeaponsPanel();
    navigationPanel = new NavigationPanel();
    contextMenu = new ContextMenu();
    navigatorPanel = new NavigatorPanel();

    addActor(weaponsPanel);
    addActor(navigationPanel);
    addActor(contextMenu);
    addActor(navigatorPanel);
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

  public boolean isInBattleState() {
    return getWeaponsPanel().isActive();
  }
}
