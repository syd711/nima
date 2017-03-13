package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.starsailor.model.SpineData;

/**
 * Component implementation for Spines.
 */
public class SpineComponent implements Component, Pool.Poolable {

  private SpineData spineData;

  @Override
  public void reset() {
    spineData = null;
  }

  public SpineData getSpineData() {
    return spineData;
  }

  public void setSpineData(SpineData spineData) {
    this.spineData = spineData;
  }
}
