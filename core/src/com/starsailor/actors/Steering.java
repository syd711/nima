package com.starsailor.actors;

import java.util.Arrays;
import java.util.List;

/**
 * The fractions of the game
 */
public enum Steering {
  GUARD, ROUTE, SEEK_AND_DESTROY;

  public static List<String> asStringList() {
    return Arrays.asList(GUARD.name(), ROUTE.name(), SEEK_AND_DESTROY.name());
  }
}
