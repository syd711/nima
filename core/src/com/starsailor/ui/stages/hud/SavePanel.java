package com.starsailor.ui.stages.hud;

import com.starsailor.util.GameTimer;
import com.starsailor.util.GameTimerExpiredListener;

/**
 * Shows the save icon
 */
public class SavePanel extends HudPanel {

  public SavePanel() {
    super("save", Position.TOP_LEFT);
  }

  @Override
  public void activate() {
    super.activate();

    new GameTimer(1000).withListener(new GameTimerExpiredListener() {
      @Override
      public void expired() {
        deactivate();
      }
    });
  }
}
