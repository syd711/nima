package com.starsailor.ui.stages.hud;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.starsailor.Game;
import com.starsailor.ui.stages.hud.navigation.NavigationPanel;
import com.starsailor.ui.stages.hud.weapons.WeaponsPanel;

/**
 *
 */
public class HudStage extends Stage {

  private final NavigationPanel navigationPanel;
  private final WeaponsPanel weaponsPanel;

  public HudStage() {
    Game.inputManager.addInputProcessor(this);

    weaponsPanel = new WeaponsPanel();
    navigationPanel = new NavigationPanel();
    addActor(weaponsPanel);
    addActor(navigationPanel);
  }

  public WeaponsPanel getWeaponsPanel() {
    return weaponsPanel;
  }

  public NavigationPanel getNavigationPanel() {
    return navigationPanel;
  }
}
