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
  private String shipType;

  @Expose
  private String formationOwner;

  @Expose
  private String defaultSteering;

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
}
