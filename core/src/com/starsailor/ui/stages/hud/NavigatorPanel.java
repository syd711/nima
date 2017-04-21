package com.starsailor.ui.stages.hud;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.starsailor.GameStateManager;
import com.starsailor.actors.Player;
import com.starsailor.actors.states.player.PlayerStates;
import com.starsailor.ui.Scene2dFactory;
import com.starsailor.ui.UIManager;

/**
 * The display on the top of the screen
 */
public class NavigatorPanel extends HudPanel {

  private final TextButton confirmButton;
  private final TextButton cancelButton;

  public NavigatorPanel() {
    super("navigator_bg", Position.TOP);

    Label nameLabel = Scene2dFactory.createLabel("Navigating to :");
    add(nameLabel);
    row();

    confirmButton = Scene2dFactory.createButton("Land");
    confirmButton.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        Player.getInstance().changeState(PlayerStates.DOCK_TO_STATION);
      }
    });
    add(confirmButton);
    confirmButton.setVisible(false);

    cancelButton = Scene2dFactory.createButton("Cancel");
    cancelButton.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        GameStateManager.getInstance().setPaused(false);
        UIManager.getInstance().getHudStage().getNavigatorPanel().setEnabled(false);
        Player.getInstance().setTarget(null);
        deactivate();
      }
    });
    add(cancelButton);
    cancelButton.setVisible(false);
  }

  public void setEnabled(boolean b) {
    this.confirmButton.setVisible(b);
    this.cancelButton.setVisible(b);
  }
}
