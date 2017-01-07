package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.nima.Game;
import com.nima.profiles.WeaponProfile;

public class ShootingComponent implements Component, Poolable {
  public WeaponProfile weaponProfile;

  @Override
  public void reset() {
    weaponProfile = null;
  }

  public boolean isCharged() {
    return Game.currentTimeMillis - weaponProfile.lastBulletTime > weaponProfile.rechargeTime;
  }
}
