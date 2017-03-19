package com.starsailor.ui.states;

import com.starsailor.Game;
import com.starsailor.actors.Player;
import com.starsailor.managers.EntityManager;
import com.starsailor.systems.LightSystem;
import com.starsailor.ui.UIManager;
import com.starsailor.ui.stages.GameStage;

/**
 * Entered when the user selects a ship
 */
public class LeaveMapState extends UIState {

  private final String mapName;

  public LeaveMapState(String mapName) {
    this.mapName = mapName;
  }

  @Override
  public void enter(GameStage entity) {
    Game.inputManager.setNavigationEnabled(false);

    UIManager.getInstance().getHudStage().getNavigatorPanel().deactivate();

    LightSystem lightSystem = EntityManager.getInstance().getLightSystem();
    lightSystem.fadeOut(true);
  }

  @Override
  public void update(GameStage entity) {
    LightSystem lightSystem = EntityManager.getInstance().getLightSystem();
    if(lightSystem.isOutFaded() && !Player.getInstance().scalingComponent.isChanging()) {
      EntityManager.getInstance().resetMapEntities();
      UIManager.getInstance().changeState(new EnterMapState(mapName));
    }
  }
}
