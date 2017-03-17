package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.starsailor.model.WeaponData;
import com.starsailor.util.GameTimer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShootingComponent implements Component, Poolable {
  private List<WeaponData> weaponDatas = new ArrayList<>();
  private Map<WeaponData, GameTimer> lastBulletTimes = new HashMap<>();

  @Override
  public void reset() {
    lastBulletTimes.clear();
    weaponDatas.clear();
  }

  public void setWeaponDatas(List<WeaponData> weaponDatas) {
    this.weaponDatas = weaponDatas;
    for(WeaponData weaponData : this.weaponDatas) {
      float rechargeTimeMillis = weaponData.getRechargeTimeMillis();
      if(rechargeTimeMillis > 0) {
        lastBulletTimes.put(weaponData, new GameTimer(rechargeTimeMillis));
      }
    }
  }

  public boolean isCharged(WeaponData weaponData) {
    GameTimer timer = lastBulletTimes.get(weaponData);
    return timer.isExpired();
  }

  public void updateLastBulletTime(WeaponData weaponData) {
    GameTimer timer = lastBulletTimes.get(weaponData);
    timer.reset();
  }

  public float getChargingPercentage(WeaponData weaponData) {
    if(lastBulletTimes.containsKey(weaponData)) {
      GameTimer timer = lastBulletTimes.get(weaponData);
      float current = timer.getDeltaTimeMillis();
      if(current > weaponData.getRechargeTimeMillis()) {
        return 100;
      }
      return Math.round(current * 100 / weaponData.getRechargeTimeMillis());
    }
    return 100; //100 percent
  }
}
