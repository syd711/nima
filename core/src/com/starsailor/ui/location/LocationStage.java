package com.starsailor.ui.location;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.starsailor.Game;

/**
 *
 */
public class LocationStage extends Stage {
  private final Texture bground;
  public final StatusTable statusTable;

  public LocationStage() {
    bground = new Texture(Gdx.files.internal("stations/background/planet.jpg"));

    statusTable = new StatusTable();
    addActor(statusTable);

    Game.inputManager.addInputProcessor(this);
  }

  @Override
  public void draw() {
    super.draw();
    getBatch().begin();
    getBatch().draw(bground, 0, 0, 1050, 1050);
    getBatch().end();
  }
}
