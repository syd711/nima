package com.starsailor.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.starsailor.actors.Player;
import com.starsailor.actors.Selectable;
import com.starsailor.actors.states.PlayerState;
import com.starsailor.managers.SelectionChangeListener;
import com.starsailor.managers.SelectionManager;
import com.starsailor.ui.hud.HudStage;
import com.starsailor.ui.location.LocationStage;
import com.starsailor.util.Resources;

/**
 * The game HUD
 */
public class Hud implements SelectionChangeListener {
  private HudStage hudStage;
  private LocationStage locationStage;

  public static Skin skin;

  public Hud() {
    skin = new Skin(Gdx.files.internal(Resources.SCENE2D_SKIN));

    hudStage = new HudStage();
    locationStage = new LocationStage();

    SelectionManager.getInstance().addSelectionChangeListener(this);
  }

  public void render() {
    getActiveStage().act();
    getActiveStage().draw();
  }

  @Override
  public void selectionChanged(Selectable oldSelection, Selectable newSelection) {
    if(newSelection != null) {
      hudStage.activateActionPanel(newSelection);
    }
    else {
      hudStage.deactivateActionPanel();
    }

  }

  private Stage getActiveStage() {
    if(Player.getInstance().getStateMachine().getCurrentState().equals(PlayerState.DOCKED)) {
      return locationStage;
    }
    return hudStage;
  }
}
