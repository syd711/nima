package com.starsailor.ui.stages.hud;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.starsailor.Game;

/**
 *
 */
public class HudStage extends Stage {

  private com.starsailor.ui.stages.hud.weapons.WeaponsPanel weaponsPanel;

  public HudStage() {
    Game.inputManager.addInputProcessor(this);

    weaponsPanel = new com.starsailor.ui.stages.hud.weapons.WeaponsPanel(this);
    addActor(weaponsPanel);
  }

  public com.starsailor.ui.stages.hud.weapons.WeaponsPanel getWeaponsPanel() {
    return weaponsPanel;
  }
}
