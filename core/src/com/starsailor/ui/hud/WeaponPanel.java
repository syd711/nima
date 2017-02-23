package com.starsailor.ui.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.starsailor.actors.*;
import com.starsailor.components.ShootingComponent;
import com.starsailor.data.WeaponData;
import com.starsailor.managers.BulletManager;
import com.starsailor.managers.SelectionManager;
import com.starsailor.managers.TextureManager;
import com.starsailor.ui.Hud;
import com.starsailor.util.Settings;

import java.util.List;

/**
 * The display on top of the screen
 */
public class WeaponPanel extends Table {
  public static final float PANEL_HEIGHT = 120f;
  public static final float SHOW_DELAY = 0.2f;

  private boolean activated = false;
  private boolean debug = Settings.getInstance().debug;

  private HudStage hudStage;

  public WeaponPanel(HudStage hudStage) {
    this.hudStage = hudStage;
    setDebug(debug);
    addWeapons();
    setPosition(Gdx.graphics.getWidth() / 2, -(PANEL_HEIGHT / 2));
  }

  private void addWeapons() {

    List<WeaponData> weapons = Player.getInstance().getWeapons();
    ShootingComponent shootingComponent = Player.getInstance().shootingComponent;
    for(int i = 0; i < weapons.size(); i++) {
      final int index = i;
      WeaponData weaponData = weapons.get(i);

      add(new Actor() {
        @Override
        public void draw(Batch batch, float parentAlpha) {
          Texture texture = TextureManager.getInstance().getTexture("healthbg");

          float percent = shootingComponent.getChargingState(weaponData);

          batch.draw(texture, getX() + (index * 10), getY() - 50, 50*percent/100, 12);
        }
      });

      TextButton weapon1 = new TextButton(weaponData.name, Hud.skin);
      weapon1.setDebug(debug);
      weapon1.setWidth(150);
      weapon1.addListener(new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
          Selectable selection = SelectionManager.getInstance().getSelection();
          if(selection instanceof NPC) {
            List<WeaponData> chargedWeapons = Player.getInstance().getChargedWeapons();
            if(chargedWeapons.contains(weaponData)) {
              BulletManager.getInstance().create(Player.getInstance(), (Ship) selection, weaponData);
            }
          }
        }
      });
      add(weapon1);
    }
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
