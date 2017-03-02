package com.starsailor.model.items;

import com.google.gson.annotations.Expose;

public class ShipItem extends MapItem<ShipItem> {

  public ShipItem(int id, ShipItem parent) {
    super(id, parent.getName());
  }

  @Expose
  private String fraction;

  @Expose
  private String shipType;

  @Expose
  private String formationOwner;

  @Expose
  private String defaultSteering;


  public String getFraction() {
    if(this.fraction == null) {
      return getParent().getFraction();
    }
    return fraction;
  }

  public void setFraction(String fraction) {
    this.fraction = fraction;
  }

  public String getShipType() {
    if(shipType == null) {
      return getParent().getShipType();
    }
    return shipType;
  }

  public void setShipType(String shipType) {
    this.shipType = shipType;
  }

  public String getFormationOwner() {
    return formationOwner;
  }

  public void setFormationOwner(String formationOwner) {
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
}
