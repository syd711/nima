package com.starsailor.actors;

import java.util.Arrays;
import java.util.List;

/**
 * Animation enum for shield spines.
 */
public enum SpineShieldAnimations {
  Schild;

  public static List<String> asStringList() {
    return Arrays.asList(Schild.toString());
  }
}
