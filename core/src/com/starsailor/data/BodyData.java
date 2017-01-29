package com.starsailor.data;

/**
 * Simplification of a box2d body
 */
public class BodyData {
  public float linearDamping;
  public float angularDamping;
  public int width;
  public int height;
  public int radius;
  public float density = 1;
  public boolean sensor = false;
}
