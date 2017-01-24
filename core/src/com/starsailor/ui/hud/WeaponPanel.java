package com.starsailor.ui.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.starsailor.actors.GameEntity;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Player;
import com.starsailor.managers.SelectionManager;
import com.starsailor.ui.Hud;
import com.starsailor.util.Settings;

/**
 * The display on top of the screen
 */
public class WeaponPanel extends Table {
  public static final float PANEL_HEIGHT = 120f;
  public static final float SHOW_DELAY = 0.2f;

  private final TextButton weapon1;
  private final TextButton weapon2;

  private boolean activated = false;

  private HudStage hudStage;

  public WeaponPanel(HudStage hudStage) {
    this.hudStage = hudStage;
    setDebug(Settings.getInstance().debug);

    weapon1 = new TextButton("Laser", Hud.skin);
    add(weapon1);
    weapon1.addListener(new ChangeListener() {
      @Override
      public void changed (ChangeEvent event, Actor actor) {
        GameEntity selection = SelectionManager.getInstance().getSelection();
        if(selection instanceof NPC) {
          Player.getInstance().switchWeapon(1);
          Player.getInstance().fireAt((NPC)selection);
        }
      }
    });

    weapon2 = new TextButton("Rocket", Hud.skin);
    add(weapon2);
    weapon2.addListener(new ChangeListener() {
      @Override
      public void changed (ChangeEvent event, Actor actor) {
        GameEntity selection = SelectionManager.getInstance().getSelection();
        if(selection instanceof NPC) {
          Player.getInstance().switchWeapon(2);
          Player.getInstance().fireAt((NPC)selection);
        }
      }
    });

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
