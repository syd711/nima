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
  private WeaponProfile activeWeaponProfile;
  private Map<WeaponProfile,Long> lastBulletTimes = new HashMap<>();

  @Override
  public void reset() {
    activeWeaponProfile = null;
    lastBulletTimes.clear();
    weaponProfiles.clear();
  }

  public void setActiveWeaponProfile(WeaponProfile profile) {
    activeWeaponProfile = profile;
  }

  public void setWeaponProfiles(List<WeaponProfile> weaponProfiles) {
    this.weaponProfiles = weaponProfiles;
  }

  public boolean isCharged() {
    WeaponProfile weaponProfile = getActiveWeaponProfile();
    long lastBulletTime = 0;
    if(lastBulletTimes.containsKey(weaponProfile)) {
      lastBulletTime = lastBulletTimes.get(weaponProfile);
    }
    float current = Game.currentTimeMillis - lastBulletTime;
    return current > weaponProfile.rechargeTimeMillis;
  }

  public WeaponProfile getActiveWeaponProfile() {
    if(activeWeaponProfile == null) {
      activeWeaponProfile = weaponProfiles.get(0);
    }
    return activeWeaponProfile;
  }

  public void updateLastBulletTime() {
     lastBulletTimes.put(activeWeaponProfile, Game.currentTimeMillis);
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
