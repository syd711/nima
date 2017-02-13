package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.starsailor.Game;
import com.starsailor.data.WeaponProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShootingComponent implements Component, Poolable {
  private List<WeaponProfile> weaponProfiles = new ArrayList<>();
  private Map<WeaponProfile, Long> lastBulletTimes = new HashMap<>();

  @Override
  public void reset() {
    lastBulletTimes.clear();
    weaponProfiles.clear();
  }

  public void setWeaponProfiles(List<WeaponProfile> weaponProfiles) {
    this.weaponProfiles = weaponProfiles;
  }

  public boolean isCharged(WeaponProfile weaponProfile) {
    long lastBulletTime = 0;
    if(lastBulletTimes.containsKey(weaponProfile)) {
      lastBulletTime = lastBulletTimes.get(weaponProfile);
    }
    float current = Game.currentTimeMillis - lastBulletTime;
    return current > weaponProfile.rechargeTimeMillis;
  }

  public void updateLastBulletTime(WeaponProfile weaponProfile) {
    lastBulletTimes.put(weaponProfile, Game.currentTimeMillis);
  }

  public float getChargingState(WeaponProfile weaponProfile) {
    long lastBulletTime = 0;
    if(lastBulletTimes.containsKey(weaponProfile)) {
      lastBulletTime = lastBulletTimes.get(weaponProfile);
      float current = Game.currentTimeMillis - lastBulletTime;
      if(current > weaponProfile.rechargeTimeMillis) {
        return 100;
      }
      return current * 100 / weaponProfile.rechargeTimeMillis;
    }
    return 100; //100 percent
  }
}
