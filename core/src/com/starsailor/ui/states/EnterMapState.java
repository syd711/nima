package com.starsailor.ui.states;

import com.starsailor.Game;
import com.starsailor.managers.EntityManager;
import com.starsailor.render.MapManager;
import com.starsailor.systems.LightSystem;
import com.starsailor.ui.UIManager;
import com.starsailor.ui.stages.GameStage;

/**
 * Entered when the user selects a ship
 */
public class EnterMapState extends UIState {
  private String mapName;

  public EnterMapState(String mapName) {
    this.mapName = mapName;
  }

  @Override
  public void enter(GameStage entity) {
    MapManager.getInstance().loadMap(mapName);

    Game.inputManager.setNavigationEnabled(true);

    LightSystem lightSystem = EntityManager.getInstance().getLightSystem();
    lightSystem.fadeOut(false);

    UIManager.getInstance().switchToHudState();
  }

  @Override
  public void update(GameStage entity) {

  }
}
