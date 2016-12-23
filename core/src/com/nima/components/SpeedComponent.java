package com.nima.components;

import com.badlogic.ashley.core.Component;

/**
 * The current speed of an entity
 */
public class SpeedComponent implements Component {

  public int value;

  public SpeedComponent(int value) {
    this.value = value;
  }
}
