package com.nima.render.model;

import java.util.List;

/**
 *
 */
public class JsonLayer {
  private List<Number> data;
  private String name;
  private int opacity;
  private int width;
  private int height;
  private int x;
  private int y;
  private String type;
  private boolean visible;

  public List<Number> getData() {
    return data;
  }

  public String getName() {
    return name;
  }

  public int getOpacity() {
    return opacity;
  }

  public String getType() {
    return type;
  }

  public boolean isVisible() {
    return visible;
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }
}
