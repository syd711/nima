package com.starsailor.data;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains all attributes a ship can have.
 */
public class ShipData extends GameData<ShipData> {

  public enum Types {
    MERCHANT, PIRATE, CRUSADER
  }

  public ShipData(int id) {
    this.id = id;
  }

  @Expose
  private int id;
  @Expose
  private String name;

  //Behaviour attributes
  @Expose
  private float attackDistance;
  @Expose
  private float shootDistance;
  @Expose
  private float retreatDistance;
  @Expose
  private float formationDistance;

  @Expose
  private float health;

  //Box2d
  @Expose
  private BodyData bodyData;

  //Spine
  @Expose
  private SpineData spineData;

  //weapons
  private List<String> weapons = new ArrayList<>();
  private transient List<WeaponData> weaponDatas = new ArrayList<>();

  //shield
  @Expose
  private String shield;

  private ShieldData shieldData;

  //Steering
  @Expose
  private SteeringData steeringData;

  public ShipData.Types getType() {
    return Types.valueOf(name.toUpperCase());
  }

  public void addWeaponProfile(WeaponData profile) {
    if(weaponDatas == null) {
      weaponDatas = new ArrayList<>();
    }
    weaponDatas.add(profile);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public float getAttackDistance() {
    return attackDistance;
  }

  public void setAttackDistance(float attackDistance) {
    this.attackDistance = attackDistance;
  }

  public float getShootDistance() {
    return shootDistance;
  }

  public void setShootDistance(float shootDistance) {
    this.shootDistance = shootDistance;
  }

  public float getRetreatDistance() {
    return retreatDistance;
  }

  public void setRetreatDistance(float retreatDistance) {
    this.retreatDistance = retreatDistance;
  }

  public float getFormationDistance() {
    return formationDistance;
  }

  public void setFormationDistance(float formationDistance) {
    this.formationDistance = formationDistance;
  }

  public float getHealth() {
    return health;
  }

  public void setHealth(float health) {
    this.health = health;
  }

  public BodyData getBodyData() {
    if(bodyData == null) {
      return getParent().getBodyData();
    }
    return bodyData;
  }

  public void setBodyData(BodyData bodyData) {
    this.bodyData = bodyData;
  }

  public List<String> getWeapons() {
    return weapons;
  }

  public void setWeapons(List<String> weapons) {
    this.weapons = weapons;
  }

  public List<WeaponData> getWeaponDatas() {
    return weaponDatas;
  }

  public void setWeaponDatas(List<WeaponData> weaponDatas) {
    this.weaponDatas = weaponDatas;
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

  public SteeringData getSteeringData() {
    if(steeringData == null) {
      return getParent().getSteeringData();
    }
    return steeringData;
  }

  public void setSteeringData(SteeringData steeringData) {
    this.steeringData = steeringData;
  }

  public SpineData getSpineData() {
    if(spineData == null) {
      return getParent().getSpineData();
    }
    return spineData;
  }

  public void setSpineData(SpineData spineData) {
    this.spineData = spineData;
  }

  public boolean isBodyDataExtended() {
    return bodyData == null;
  }

  public boolean isSteeringDataExtended() {
    return steeringData == null;
  }

  public boolean isSpineDataExtended() {
    return spineData == null;
  }

  @Override
  public String toString() {
    return name;
  }

}
