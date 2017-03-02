package com.starsailor.actors;

import java.util.Arrays;
import java.util.List;

/**
 * The fractions of the game
 */
public enum Fraction {
  PLAYER, ALOPE, ETYR;

  public static List<String> asStringList() {
    return Arrays.asList(ALOPE.name(), ETYR.name());
  }
}
