package com.starsailor.ui.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.starsailor.actors.GameEntity;
import com.starsailor.ui.Hud;
import com.starsailor.util.Settings;

/**
 * The display on top of the screen
 */
public class ActionPanel extends Table {
  public static final float PANEL_HEIGHT = 120f;
  public static final float SHOW_DELAY = 0.2f;
  private final Texture bground;

  private final TextButton attackButton;
  private final TextButton tradeButton;

  private boolean activated = false;

  public ActionPanel() {
    bground = new Texture(Gdx.files.internal("stations/background/planet.jpg"));
    TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(new TextureRegion(bground));

    setDebug(Settings.DEBUG);
    setBackground(textureRegionDrawable);


    attackButton = new TextButton("Attack", Hud.skin);
    add(attackButton);

    tradeButton = new TextButton("Trade", Hud.skin);
    add(tradeButton);

    setPosition(Gdx.graphics.getWidth()/2, -(PANEL_HEIGHT/2));
  }

  public void activate(GameEntity entity) {
    if(!activated) {
      activated = true;
      addAction(Actions.moveBy(0, PANEL_HEIGHT, SHOW_DELAY));
    }

  }

  public void deactivate() {
    if(activated) {
      activated = false;
      addAction(Actions.moveBy(0, -PANEL_HEIGHT, SHOW_DELAY));
    }
  }
}
