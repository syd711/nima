package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Marker component for all entities that one or more spine components to render.
 * This is just to collect the family.
 */
public class SpineMarkerComponent implements Component, Pool.Poolable {
  @Override
  public void reset() {

  }
}
