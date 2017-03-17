package com.starsailor.ui.states;

import com.badlogic.ashley.core.Entity;
import com.starsailor.actors.Player;
import com.starsailor.managers.UIManager;
import com.starsailor.ui.stages.GameStage;
import com.starsailor.ui.stages.hud.HudStage;

/**
 * The UI state entered when the user enters a location entity
 */
public class LocationState extends UIState {
  @Override
  public void enter(GameStage entity) {
    HudStage hudStage = UIManager.getInstance().getHudStage();
    hudStage.getContextMenu().hide();
    hudStage.getNavigationPanel().deactivate();
    hudStage.getNavigatorPanel().deactivate();

    Entity target = Player.getInstance().getTarget();
//    UIManager.getInstance().getLocationStage().show(target);
  }
}
