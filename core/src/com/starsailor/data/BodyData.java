package com.starsailor.data;

import com.google.gson.annotations.Expose;

/**
 * Simplification of a box2d body
 */
public class BodyData extends GameData {
  @Expose
  private float linearDamping;
  @Expose
  private float angularDamping;
  @Expose
  private float width;
  @Expose
  private float height;
  @Expose
  private float radius;
  @Expose
  private float density = 1;
  @Expose
  private boolean sensor = false;

  public BodyData() {

  }

  public BodyData(BodyData bodyData) {
    this.linearDamping = bodyData.getLinearDamping();
    this.angularDamping = bodyData.getAngularDamping();
    this.width = bodyData.getWidth();
    this.height = bodyData.getHeight();
    this.radius = bodyData.getRadius();
    this.density = bodyData.getDensity();
    this.sensor = bodyData.isSensor();
    this.setExtendParentData(true);
  }

  public float getLinearDamping() {
    return linearDamping;
  }

  public void setLinearDamping(float linearDamping) {
    this.linearDamping = linearDamping;
  }

  public float getAngularDamping() {
    return angularDamping;
  }

  public void setAngularDamping(float angularDamping) {
    this.angularDamping = angularDamping;
  }

  public float getHeight() {
    return height;
  }

  public void setHeight(float height) {
    this.height = height;
  }

  public float getRadius() {
    return radius;
  }

  public void setRadius(float radius) {
    this.radius = radius;
  }

  public float getDensity() {
    return density;
  }

  public void setDensity(float density) {
    this.density = density;
  }

  public boolean isSensor() {
    return sensor;
  }

  public void setSensor(boolean sensor) {
    this.sensor = sensor;
  }

  public float getWidth() {
    return width;
  }

  public void setWidth(float width) {
    this.width = width;
  }

  @Override
  public String toString() {
    return "Body Data";
  }
}
