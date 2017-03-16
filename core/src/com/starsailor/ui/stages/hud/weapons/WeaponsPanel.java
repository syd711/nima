package com.starsailor.ui.stages.hud.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.starsailor.actors.Player;
import com.starsailor.components.ShootingComponent;
import com.starsailor.managers.ResourceManager;
import com.starsailor.model.WeaponData;
import com.starsailor.ui.Scene2dFactory;
import com.starsailor.ui.stages.hud.HudPanel;

import java.util.List;

/**
 * The display on top of the screen
 */
public class WeaponsPanel extends HudPanel {

  public WeaponsPanel() {
    super("weapons_bg", Position.TOP);

    //add all available weapons
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
          batch.draw(texture, getX() + (index * 10), getY() - 50, 50 * percent / 100, 12);
        }
      });


      TextButton weaponButton = Scene2dFactory.createButton(weaponData.getName(), new WeaponPanelChangeListener());
      weaponButton.setUserObject(weaponData);
      add(weaponButton);
    }
  }
}
