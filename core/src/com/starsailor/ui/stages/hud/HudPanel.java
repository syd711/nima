package com.starsailor.ui.stages.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.starsailor.managers.ResourceManager;
import com.starsailor.util.Settings;

/**
 * Common panel class for all HUD elements
 */
public class HudPanel extends Table {
  public static final float SHOW_DELAY = 0.2f;
  private final Texture bground;
  private Position position;

  private boolean activated = false;

  public enum Position {
    TOP, TOP_LEFT, LEFT, BOTTOM, RIGHT;

  }
  public HudPanel(String textureName, Position position) {
    this.position = position;
    bground = ResourceManager.getInstance().getTextureAsset(textureName);
    TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(new TextureRegion(bground));
    setWidth(bground.getWidth());
    setHeight(bground.getHeight());

    setDebug(Settings.getInstance().debug);
    setBackground(textureRegionDrawable);

    if(position.equals(Position.BOTTOM)) {
      setPosition(Gdx.graphics.getWidth() / 2 - bground.getWidth() / 2, -bground.getHeight());
    }
    else if(position.equals(Position.LEFT)) {
      setPosition(-bground.getWidth()-1, Gdx.graphics.getHeight() / 2 - bground.getHeight() / 2);
    }
    else if(position.equals(Position.TOP)) {
      setPosition(Gdx.graphics.getWidth() / 2 - bground.getWidth() / 2, Gdx.graphics.getHeight()+1);
    }
    else if(position.equals(Position.TOP_LEFT)) {
      setPosition(10, Gdx.graphics.getHeight()+1);
    }
    else if(position.equals(Position.RIGHT)) {
      setPosition(Gdx.graphics.getWidth()+1, Gdx.graphics.getHeight() / 2 - bground.getHeight() / 2);
    }
  }

  public void toggle() {
    if(activated) {
      deactivate();
    }
    else {
      activate();
    }
  }

  public boolean isActive() {
    return activated;
  }

  public void activate() {
    if(activated) {
      return;
    }
    activated = true;

    if(position.equals(Position.BOTTOM)) {
      addAction(Actions.moveBy(0, bground.getHeight(), SHOW_DELAY));
    }
    else if(position.equals(Position.LEFT)) {
      addAction(Actions.moveBy(bground.getWidth(), 0, SHOW_DELAY));
    }
    else if(position.equals(Position.TOP)) {
      addAction(Actions.moveBy(0, -bground.getHeight(), SHOW_DELAY));
    }
    else if(position.equals(Position.TOP_LEFT)) {
      addAction(Actions.moveBy(0, -bground.getHeight(), SHOW_DELAY));
    }
    else if(position.equals(Position.RIGHT)) {
      addAction(Actions.moveBy(-bground.getWidth(), 0, SHOW_DELAY));
    }
  }

  public void deactivate() {
    if(!activated) {
      return;
    }
    activated = false;

    if(position.equals(Position.BOTTOM)) {
      addAction(Actions.moveBy(0, -bground.getHeight(), SHOW_DELAY));
    }
    else if(position.equals(Position.LEFT)) {
      addAction(Actions.moveBy(-bground.getWidth(), 0, SHOW_DELAY));
    }
    else if(position.equals(Position.TOP)) {
      addAction(Actions.moveBy(0, bground.getHeight(), SHOW_DELAY));
    }
    else if(position.equals(Position.TOP_LEFT)) {
      addAction(Actions.moveBy(0, bground.getHeight(), SHOW_DELAY));
    }
    else if(position.equals(Position.RIGHT)) {
      addAction(Actions.moveBy(bground.getWidth(), 0, SHOW_DELAY));
    }

  }
}
