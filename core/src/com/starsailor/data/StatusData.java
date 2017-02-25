package com.starsailor.data;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class StatusData extends GameData {
  //shield
  @Expose
  private String shield;
  @Expose
  private float health;
  //@Expose
  private List<String> weapons = new ArrayList<>();

  private ShieldData shieldData;

  private transient List<WeaponData> weaponDatas = new ArrayList<>();

  public StatusData() {
  }

  public StatusData(StatusData statusData) {
    this.health = statusData.getHealth();
    this.shield = statusData.getShield();
    this.weapons = new ArrayList<>(statusData.getWeapons());
  }

  public void addWeaponProfile(WeaponData profile) {
    if(weaponDatas == null) {
      weaponDatas = new ArrayList<>();
    }
    weaponDatas.add(profile);
  }

  public List<WeaponData> getWeaponDatas() {
    return weaponDatas;
  }

  public void setWeaponDatas(List<WeaponData> weaponDatas) {
    this.weaponDatas = weaponDatas;
  }

  public List<String> getWeapons() {
    return weapons;
  }

  public void setWeapons(List<String> weapons) {
    this.weapons = weapons;
  }

  public float getHealth() {
    return health;
  }

  public void setHealth(float health) {
    this.health = health;
  }


  public String getShield() {
    return shield;
  }

  public void setShield(String shield) {
    this.shield = shield;
  }

  public ShieldData getShieldData() {
    return shieldData;
  }

  public void setShieldData(ShieldData shieldData) {
    this.shieldData = shieldData;
  }
}
