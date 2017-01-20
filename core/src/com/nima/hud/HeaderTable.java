package com.nima.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.nima.actors.Player;
import com.nima.actors.states.PlayerState;

/**
 * The display on top of the screen
 */
public class HeaderTable extends Table {
  public Button dockButton;
  private final Label stationLabel;

  public HeaderTable() {
    setDebug(true);
    top();
    setFillParent(true);

    stationLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    add(stationLabel).expandX().padTop(20);


    TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
    style.font = new BitmapFont();
    style.fontColor = Color.RED;
    dockButton = new TextButton("Leave Station", style);
    dockButton.padTop(20);
    dockButton.padRight(20);
    dockButton.addListener(new ChangeListener() {
      @Override
      public void changed (ChangeEvent event, Actor actor) {
        Player.getInstance().getStateMachine().changeState(PlayerState.UNDOCK_FROM_STATION);
      }
    });
    add(dockButton).expandX().align(Align.right);

    dockButton.setVisible(false);
  }
}
