package com.starsailor.model;

import com.google.gson.annotations.Expose;

/**
 * Contains all attributes a ship can have.
 */
public class ShipData extends GameDataWithId<ShipData> {

  public enum Types {
    MERCHANT, PIRATE, CRUSADER
  }

  public ShipData(int id) {
    super(id, null);
  }

  //Distance triggers
  @Expose
  private DistanceData distanceData;

  //Box2d
  @Expose
  private BodyData bodyData;

  //Spine
  @Expose
  private SpineData spineData;

  //Ship health and shield status
  @Expose
  private StatusData statusData;

  //Steering
  @Expose
  private SteeringData steeringData;

  @Deprecated
  public ShipData.Types getType() {
    return null;
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

  public DistanceData getDistanceData() {
    if(distanceData == null) {
      return getParent().getDistanceData();
    }
    return distanceData;
  }

  public StatusData getStatusData() {
    if(statusData == null) {
      return getParent().getStatusData();
    }
    return statusData;
  }

  public void setStatusData(StatusData statusData) {
    this.statusData = statusData;
  }

  public void setDistanceData(DistanceData distanceData) {
    this.distanceData = distanceData;
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

  public boolean isDistanceDataExtended() {
    return distanceData == null;
  }

  public boolean isStatusDataExtended() {
    return statusData == null;
  }
}
