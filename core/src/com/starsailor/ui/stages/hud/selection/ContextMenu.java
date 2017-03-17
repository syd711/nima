package com.starsailor.ui.stages.hud.selection;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.starsailor.actors.Selectable;
import com.starsailor.actors.Ship;
import com.starsailor.managers.SelectionManager;
import com.starsailor.managers.UIManager;
import com.starsailor.ui.Scene2dFactory;
import com.starsailor.ui.states.UIStates;

/**
 * The popup that is shown when a selection is made
 */
public class ContextMenu extends Table {
  private final TextButton attackButton;
  private final TextButton tradeButton;
  private final TextButton cancelButton;

  public ContextMenu() {
    //add context menu
    tradeButton = Scene2dFactory.createMenuButton("Trade");

    attackButton = Scene2dFactory.createMenuButton("Attack");
    attackButton.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        UIManager.getInstance().changeState(UIStates.BATTLE_STATE);
      }
    });

    cancelButton = Scene2dFactory.createMenuButton("Cancel");
    cancelButton.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        UIManager.getInstance().switchToDefaultState();
      }
    });

    add(tradeButton).width(100);
    row();
    add(attackButton).width(100);
    row();
    add(cancelButton).width(100);
    row();
  }

  public void show(float x, float y) {
    Selectable selection = SelectionManager.getInstance().getSelection();
    Ship ship = (Ship) selection;
    tradeButton.setVisible(!ship.isInBattleState());

    setPosition(x+90, y+40);
    UIManager.getInstance().getHudStage().addActor(this);
  }

  public void hide() {
    this.remove();
  }
}