package com.starsailor.ui.stages.hud;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.starsailor.GameStateManager;
import com.starsailor.ui.Scene2dFactory;

/**
 * The display on the top of the screen
 */
public class TradingPlayerPanel extends TradingPanel {

  public TradingPlayerPanel() {
    super("inventory_bg", Position.LEFT);


    TextButton cancelButton =  Scene2dFactory.createButton("Cancel Trading");
    cancelButton.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        cancel();
      }
    });

    add(cancelButton);
  }

  @Override
  public void activate() {
    super.activate();
    GameStateManager.getInstance().setPaused(true);
  }

  @Override
  public void deactivate() {
    super.deactivate();
    GameStateManager.getInstance().setPaused(false);
  }
}
