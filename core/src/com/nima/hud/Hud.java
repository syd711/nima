package com.nima.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.nima.actors.Player;
import com.nima.actors.states.PlayerState;

/**
 * The game HUD
 */
public class Hud {

  public Stage stage;

  private final Texture bground;
  private final HeaderTable headerTable;

  public Hud() {
    stage = new Stage();

    headerTable = new HeaderTable();
    stage.addActor(headerTable);

    bground = new Texture(Gdx.files.internal("stations/background/planet.jpg"));
  }

  public void render() {
    stage.act();

    if(Player.getInstance().getStateMachine().getCurrentState().equals(PlayerState.DOCKED)) {
      headerTable.dockButton.setVisible(true);
      stage.getBatch().begin();
      stage.getBatch().draw(bground, 0, 0, 1050, 1050);
      stage.getBatch().end();
      Gdx.input.setInputProcessor(stage);
    }
    else {
      headerTable.dockButton.setVisible(false);
    }

    stage.draw();
  }
}
