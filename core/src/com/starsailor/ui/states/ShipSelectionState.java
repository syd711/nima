package com.starsailor.ui.states;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.starsailor.Game;
import com.starsailor.managers.SelectionManager;
import com.starsailor.managers.StageManager;
import com.starsailor.ui.Scene2dFactory;
import com.starsailor.ui.stages.GameStage;
import com.starsailor.util.Settings;

/**
 * Entered when the user selects a ship
 */
public class ShipSelectionState extends UIState {
  private VerticalGroup menuGroup = new VerticalGroup();
  private long startTime;
  private final TextButton attackButton;
  private final TextButton tradeButton;

  public ShipSelectionState() {
    tradeButton = Scene2dFactory.createButton("Trade");
    attackButton = Scene2dFactory.createButton("Attack");
    attackButton.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        StageManager.getInstance().changeState(UIStates.BATTLE_STATE);
      }
    });

    menuGroup.addActor(attackButton);
    menuGroup.addActor(tradeButton);
  }

  @Override
  public void enter(GameStage entity) {
    startTime = System.currentTimeMillis();

    Vector2 lastClickLocation = Game.inputManager.getLastClickLocation();
    menuGroup.setPosition(lastClickLocation.x, lastClickLocation.y);

    Stage hudStage = StageManager.getInstance().getHudStage();
    hudStage.addActor(menuGroup);
  }

  @Override
  public void update(GameStage entity) {
    long l = System.currentTimeMillis();
    if(l-startTime > Settings.CONTEXT_MENU_TIMEOUT) {
      menuGroup.remove();
      SelectionManager.getInstance().setSelection(null);
    }
  }

  @Override
  public void exit(GameStage entity) {
    menuGroup.remove();
  }
}
