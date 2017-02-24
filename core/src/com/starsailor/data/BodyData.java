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
  private int width;
  @Expose
  private int height;
  @Expose
  private int radius;
  @Expose
  private float density = 1;
  @Expose
  private boolean sensor = false;

  public BodyData() {

  }

  public BodyData(BodyData bodyData) {
    this.linearDamping = bodyData.getLinearDamping();
    this.angularDamping = bodyData.getLinearDamping();
    this.width = bodyData.getWidth();
    this.height = bodyData.getHeight();
    this.radius = bodyData.getRadius();
    this.density = bodyData.getDensity();
    this.sensor = bodyData.isSensor();
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

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getRadius() {
    return radius;
  }

  public void setRadius(int radius) {
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

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  @Override
  public String toString() {
    return "Body Data";
  }
}
