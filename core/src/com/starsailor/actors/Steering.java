package com.starsailor.actors;

import java.util.Arrays;
import java.util.List;

/**
 * The fractions of the game
 */
public enum Steering {
  ROUTE, SEEK_AND_DESTROY, WANDERING_SEEK_AND_DESTROY, FLEE, ATTACK;

  public static List<String> defaultSteeringList() {
    return Arrays.asList(ROUTE.name(), SEEK_AND_DESTROY.name());
  }

  public static List<String> battleSteeringList() {
    return Arrays.asList(FLEE.name(), ATTACK.name());
  }
}
