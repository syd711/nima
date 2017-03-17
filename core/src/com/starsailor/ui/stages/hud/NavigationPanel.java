package com.starsailor.ui.stages.hud;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.starsailor.ui.Scene2dFactory;
import com.starsailor.ui.stages.hud.HudPanel;

/**
 * The display on the left of the screen
 */
public class NavigationPanel extends HudPanel {

  public NavigationPanel() {
    super("navigation_bg", Position.RIGHT);

    TextButton dockButton = Scene2dFactory.createButton("Leave Station");

    Label nameLabel = Scene2dFactory.createLabel("Name:");
    TextButton nameText =  Scene2dFactory.createButton("Athena Galaxy");
    nameText.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        System.out.println("galaxy change");
      }
    });

    Label addressLabel =  Scene2dFactory.createLabel("Address:");
    TextButton addressText =  Scene2dFactory.createButton("");

    add(nameLabel);
    add(nameText).width(100);
    row();
    add(addressLabel);
    add(addressText).width(100);

    top().left();
  }
}
