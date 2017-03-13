package com.starsailor.actors;

import java.util.Arrays;
import java.util.List;

/**
 * Animation enum for ship spines.
 */
public enum SpineShipAnimations {
  Move, Stand;

  public static List<String> asStringList() {
    return Arrays.asList(Move.toString(), Stand.toString());
  }
}
