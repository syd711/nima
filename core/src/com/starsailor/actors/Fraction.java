package com.starsailor.actors;

import java.util.Arrays;
import java.util.List;

/**
 * The fractions of the game
 */
public enum Fraction {
  PLAYER, PIRATE, ALOPE, ETYR;

  public static List<String> asStringList() {
    return Arrays.asList(PLAYER.name(), PIRATE.name(), ALOPE.name(), ETYR.name());
  }
}
