package com.starsailor.ui.stages.hud;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Player;
import com.starsailor.actors.Selectable;
import com.starsailor.actors.Ship;
import com.starsailor.managers.BulletManager;
import com.starsailor.managers.SelectionManager;
import com.starsailor.model.WeaponData;

import java.util.List;

/**
 * Created by Matthias on 16.03.2017.
 */
public class WeaponPanelChangeListener extends ChangeListener {


  @Override
  public void changed(ChangeEvent event, Actor actor) {
    TextButton button = (TextButton) actor;
    Selectable selection = SelectionManager.getInstance().getSelection();
    if(selection instanceof NPC) {
      List<WeaponData> chargedWeapons = Player.getInstance().getChargedWeapons();
      WeaponData weaponData = (WeaponData) button.getUserObject();
      if(chargedWeapons.contains(weaponData)) {
        BulletManager.getInstance().create(Player.getInstance(), (Ship) selection, weaponData);
      }
    }
  }
}
