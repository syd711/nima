package com.starsailor.actors;

import java.util.Arrays;
import java.util.List;

/**
 * Animation enum for bullet spines.
 */
public enum SpineWeaponAnimations {
  Fly;

  public static List<String> asStringList() {
    return Arrays.asList(Fly.toString());
  }
}
