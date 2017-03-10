package com.starsailor.ui.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.starsailor.managers.I18N;
import com.starsailor.ui.Hud;
import com.starsailor.util.Settings;

/**
 * The display on top of the screen
 */
public class InfoTable extends Table {

  private final TextButton dockButton;

  public InfoTable() {
    setDebug(Settings.getInstance().debug);
    top();
    setFillParent(true);

    TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
    style.font = new BitmapFont();
    style.fontColor = Color.RED;
    dockButton = new TextButton(I18N.getBundle().get("info_button"), Hud.skin);
    dockButton.padRight(20);
    add(dockButton).expandX().align(Align.right);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    super.draw(batch, parentAlpha);
  }
}
