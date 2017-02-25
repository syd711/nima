package com.starsailor.data;

import com.google.gson.annotations.Expose;

/**
 *
 */
public class DistanceData extends GameData {
  //Behaviour attributes
  @Expose
  private float attackDistance;
  @Expose
  private float shootDistance;
  @Expose
  private float retreatDistance;
  @Expose
  private float formationDistance;

  public DistanceData() {

  }

  public DistanceData(DistanceData distanceData) {
    this.attackDistance = distanceData.getAttackDistance();
    this.shootDistance = distanceData.getShootDistance();
    this.retreatDistance = distanceData.getRetreatDistance();
    this.formationDistance = distanceData.getFormationDistance();
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
}
