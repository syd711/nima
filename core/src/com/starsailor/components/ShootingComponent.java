package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.starsailor.Game;
import com.starsailor.data.WeaponData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShootingComponent implements Component, Poolable {
  private List<WeaponData> weaponDatas = new ArrayList<>();
  private Map<WeaponData, Long> lastBulletTimes = new HashMap<>();

  @Override
  public void reset() {
    lastBulletTimes.clear();
    weaponDatas.clear();
  }

  public void setWeaponDatas(List<WeaponData> weaponDatas) {
    this.weaponDatas = weaponDatas;
  }

  public boolean isCharged(WeaponData weaponData) {
    long lastBulletTime = 0;
    if(lastBulletTimes.containsKey(weaponData)) {
      lastBulletTime = lastBulletTimes.get(weaponData);
    }
    float current = Game.currentTimeMillis - lastBulletTime;
    return current > weaponData.getRechargeTimeMillis();
  }

  public void updateLastBulletTime(WeaponData weaponData) {
    lastBulletTimes.put(weaponData, Game.currentTimeMillis);
  }

  public float getChargingState(WeaponData weaponData) {
    long lastBulletTime = 0;
    if(lastBulletTimes.containsKey(weaponData)) {
      lastBulletTime = lastBulletTimes.get(weaponData);
      float current = Game.currentTimeMillis - lastBulletTime;
      if(current > weaponData.getRechargeTimeMillis()) {
        return 100;
      }
      return current * 100 / weaponData.getRechargeTimeMillis();
    }
    return 100; //100 percent
  }
}
