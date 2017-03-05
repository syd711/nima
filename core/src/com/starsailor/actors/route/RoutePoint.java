package com.starsailor.actors.route;

import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.GameEntity;
import com.starsailor.components.ComponentFactory;
import com.starsailor.model.SteeringData;

import static com.starsailor.util.Settings.MPP;

/**
 * Model for a tracking point of a route.
 * This model is an entity although it is never added to Ashley.
 */
public class RoutePoint extends GameEntity {
  //steering defaults
  private SteeringData steeringData = new SteeringData();

  private Vector2 position;
  private boolean dockable;
  private int index;
  private Float dockTime;

  public RoutePoint(Integer index) {
    this.index = index;
    ComponentFactory.addRoutePointCollisionComponent(this);
  }

  public SteeringData getSteeringData() {
    return steeringData;
  }

  public void setSteeringData(SteeringData steeringData) {
    this.steeringData = steeringData;
  }

  public Vector2 getPosition() {
    return position;
  }

  public Vector2 getBox2dPosition() {
    return new Vector2(getPosition()).scl(MPP);
  }

  public void setPosition(Vector2 position) {
    this.position = position;
  }

  public boolean isDockable() {
    return dockable;
  }

  public void setDockable(boolean dockable) {
    this.dockable = dockable;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public Float getDockTime() {
    return dockTime;
  }

  public void setDockTime(Float dockTime) {
    this.dockTime = dockTime;
  }

  @Override
  public String toString() {
    return "Route Point " + index + " " + position;
  }
}
