package com.starsailor.ui.stages.hud.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.starsailor.actors.Player;
import com.starsailor.components.ShootingComponent;
import com.starsailor.managers.ResourceManager;
import com.starsailor.model.WeaponData;
import com.starsailor.ui.Scene2dFactory;
import com.starsailor.ui.stages.hud.HudStage;
import com.starsailor.util.Settings;

import java.util.List;

/**
 * The display on top of the screen
 */
public class WeaponsPanel extends Table {
  public static final float SHOW_DELAY = 0.2f;
  private final Texture bground;

  public WeaponsPanel(HudStage hudStage) {
    bground = ResourceManager.getInstance().getTextureAsset("weapons_bg");
    TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(new TextureRegion(bground));
    setWidth(bground.getWidth());
    setHeight(bground.getHeight());

    setDebug(Settings.getInstance().debug);
    setBackground(textureRegionDrawable);
    setPosition(Gdx.graphics.getWidth() / 2 - bground.getWidth() / 2, -bground.getHeight());
    hudStage.addActor(this);

    List<WeaponData> weapons = Player.getInstance().getWeapons();
    ShootingComponent shootingComponent = Player.getInstance().shootingComponent;
    for(int i = 0; i < weapons.size(); i++) {
      final int index = i;
      WeaponData weaponData = weapons.get(i);

      add(new Actor() {
        @Override
        public void draw(Batch batch, float parentAlpha) {
          Texture texture = ResourceManager.getInstance().getTextureAsset("healthbg");
          float percent = shootingComponent.getChargingState(weaponData);
          batch.draw(texture, getX() + (index * 10), getY() - 50, 50*percent/100, 12);
        }
      });


      TextButton weaponButton = Scene2dFactory.createButton(weaponData.getName(), new WeaponPanelChangeListener());
      weaponButton.setUserObject(weaponData);
      add(weaponButton);
    }
  }

  public void activate() {
    addAction(Actions.moveBy(0, bground.getHeight(), SHOW_DELAY));
  }

  public void deactivate() {
    addAction(Actions.moveBy(0, -bground.getHeight(), SHOW_DELAY));
  }
}
