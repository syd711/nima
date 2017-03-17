package com.starsailor.ui.stages.hud;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.starsailor.ui.Scene2dFactory;

/**
 * The display on the top of the screen
 */
public class NavigatorPanel extends HudPanel {

  public NavigatorPanel() {
    super("navigator_bg", Position.TOP);

    Label nameLabel = Scene2dFactory.createLabel("Navigating to :");
    add(nameLabel);
    row();

    TextButton confirmButton =  Scene2dFactory.createButton("Land");
    confirmButton.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        System.out.println("galaxy change");
      }
    });
    add(confirmButton);

    TextButton cancelButton =  Scene2dFactory.createButton("Cancel");
    cancelButton.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        System.out.println("galaxy change");
      }
    });

    add(cancelButton);
  }
}
