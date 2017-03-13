package com.starsailor.model;

import com.google.gson.annotations.Expose;

/**
 * Spine informations
 */
public class SpineData extends GameData {
  @Expose
  private String spine;
  @Expose
  private float scale;
  @Expose
  private String defaultAnimation;

  public SpineData() {

  }

  public SpineData(SpineData spineData) {
    this.spine = spineData.getSpine();
    this.scale = spineData.getScale();
    this.defaultAnimation = spineData.getDefaultAnimation();
    this.setExtendParentData(true);
  }

  public String getSpine() {
    return spine;
  }

  public void setSpine(String spine) {
    this.spine = spine;
  }

  public float getScale() {
    return scale;
  }

  public void setScale(float scale) {
    this.scale = scale;
  }

  public String getDefaultAnimation() {
    return defaultAnimation;
  }

  public void setDefaultAnimation(String defaultAnimation) {
    this.defaultAnimation = defaultAnimation;
  }
}
