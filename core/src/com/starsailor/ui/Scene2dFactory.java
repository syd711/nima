package com.starsailor.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.starsailor.util.Resources;
import com.starsailor.util.Settings;

/**
 * Creates new scene2d widgets
 */
public class Scene2dFactory {
  private static Skin skin = new Skin(Gdx.files.internal(Resources.SCENE2D_SKIN));

  public static TextButton createButton(String label, ChangeListener changeListener) {
    TextButton button = new TextButton(label, skin);
    button.setDebug(Settings.getInstance().debug);
    button.setWidth(150);
    button.addListener(changeListener);
    return button;
  }

  public static TextButton createButton(String label) {
    TextButton button = new TextButton(label, skin);
    return button;
  }
}
