package com.starsailor.ui.stages.hud;

import com.starsailor.ui.UIManager;

/**
 * Parent for both trading panels
 */
public class TradingPanel extends HudPanel {
  public TradingPanel(String background, Position position) {
    super(background, position);
  }

  protected void cancel() {
    UIManager.getInstance().getHudStage().getTradingNpcPanel().deactivate();
    UIManager.getInstance().getHudStage().getTradingPlayerPanel().deactivate();
    UIManager.getInstance().switchToHudState();
  }
}
