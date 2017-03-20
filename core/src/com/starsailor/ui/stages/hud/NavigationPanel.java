package com.starsailor.ui.stages.hud;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.starsailor.ui.Scene2dFactory;
import com.starsailor.ui.UIManager;
import com.starsailor.ui.states.LeaveMapState;
import com.starsailor.ui.states.UIStates;

/**
 * The display on the left of the screen
 */
public class NavigationPanel extends HudPanel {

  public NavigationPanel() {
    super("navigation_bg", Position.RIGHT);


    TextButton planet1 =  Scene2dFactory.createButton("Nereus");
    planet1.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
       UIManager.getInstance().changeState(new LeaveMapState("nereus"));
      }
    });

    TextButton planet2 =  Scene2dFactory.createButton("Erebos");
    planet2.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        UIManager.getInstance().changeState(new LeaveMapState("erebos"));
      }
    });

    Label addressLabel =  Scene2dFactory.createLabel("blah:");
    TextButton cancelButton =  Scene2dFactory.createButton("Cancel");
    cancelButton.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        UIManager.getInstance().changeState(UIStates.NAVIGATE_BACK_STATE);
      }
    });

    add(planet1).width(100);
    row();
    add(planet2).width(100);
    row();
    add(cancelButton).width(100);

    top().left();
  }
}
