package com.starsailor.ui.stages.location;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.starsailor.actors.Player;
import com.starsailor.actors.states.player.PlayerState;
import com.starsailor.ui.Scene2dFactory;

/**
 * The display on top of the screen
 */
public class StatusTable extends Table {
  public Button dockButton;
  private final Label stationLabel;

  public StatusTable() {
    setDebug(true);
    top();
    setFillParent(true);

    stationLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    add(stationLabel).expandX().padTop(20);

    dockButton = Scene2dFactory.createButton("Leave Station");
    dockButton.padTop(20);
    dockButton.padRight(20);
    dockButton.addListener(new ChangeListener() {
      @Override
      public void changed (ChangeEvent event, Actor actor) {
        Player.getInstance().getStateMachine().changeState(PlayerState.UNDOCK_FROM_STATION);
      }
    });
    add(dockButton).expandX().align(Align.right);

    dockButton.setVisible(true);
  }
}
