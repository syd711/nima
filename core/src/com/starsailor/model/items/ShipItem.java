package com.starsailor.model.items;

import com.google.gson.annotations.Expose;
import com.starsailor.model.ShipData;

public class ShipItem extends MapItem<ShipItem> {

  public ShipItem(int id, ShipItem parent) {
    super(id, parent.getName());
  }

  @Expose
  private String fraction;

  @Expose
  private int shipType;

  @Expose
  private int formationOwner;

  @Expose
  private String defaultSteering;

  @Expose
  private String battleSteering;

  @Expose
  private String route;

  //real data
  private ShipData shipData;


  public String getFraction() {
    if(this.fraction == null) {
      return getParent().getFraction();
    }
    return fraction;
  }

  public void setFraction(String fraction) {
    this.fraction = fraction;
  }

  public int getShipType() {
    if(shipType <= 0) {
      return getParent().getShipType();
    }
    return shipType;
  }

  public void setShipType(int shipType) {
    this.shipType = shipType;
  }

  public int getFormationOwner() {
    return formationOwner;
  }

  public void setFormationOwner(int formationOwner) {
    this.formationOwner = formationOwner;
  }

  @Override
  public String toString() {
    return getName() + " (" + getFraction() + ")";
  }

  public String getDefaultSteering() {
    return defaultSteering;
  }

  public void setDefaultSteering(String defaultSteering) {
    this.defaultSteering = defaultSteering;
  }

  public ShipData getShipData() {
    return shipData;
  }

  public void setShipData(ShipData shipData) {
    this.shipData = shipData;
  }

  public String getRoute() {
    return route;
  }

  public void setRoute(String route) {
    this.route = route;
  }

  public String getBattleSteering() {
    return battleSteering;
  }

  public void setBattleSteering(String battleSteering) {
    this.battleSteering = battleSteering;
  }
}
