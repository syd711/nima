package com.starsailor.model.items;

import com.google.gson.annotations.Expose;
import com.starsailor.model.GameDataWithId;

public class ShipItem extends GameDataWithId<ShipItem> {

  public ShipItem(int id, ShipItem parent) {
    super(id, parent.getName());
  }

  @Expose
  private String fraction;

  @Expose
  private String shipType;


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

  @Override
  public String toString() {
    return getName() + " (" + getFraction() + ")";
  }
}
