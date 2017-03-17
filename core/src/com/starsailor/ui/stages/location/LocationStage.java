package com.starsailor.ui.stages.location;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.starsailor.Game;
import com.starsailor.managers.ResourceManager;

/**
 *
 */
public class LocationStage extends Stage {
  private final Texture bground;
  public final StatusTable statusTable;

  public LocationStage() {
    bground = ResourceManager.getInstance().getTextureAsset("planet");

    statusTable = new StatusTable();
    addActor(statusTable);

    Game.inputManager.addInputProcessor(this);
  }

  @Override
  public void draw() {
    getBatch().begin();
    getBatch().draw(bground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    getBatch().end();
    super.draw();
  }
}
