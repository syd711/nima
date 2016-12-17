package com.nima.render.model;

import java.util.List;

/**
 *
 */
public class JsonTiledMap {
  private int height;
  private int width;
  private int tileheight;
  private int tilewidth;
  private List<JsonLayer> layers;

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  public int getTileheight() {
    return tileheight;
  }

  public int getTilewidth() {
    return tilewidth;
  }

  public List<JsonLayer> getLayers() {
    return layers;
  }

  public JsonLayer getLayer(String name) {
    for(JsonLayer layer : layers) {
      if(layer.getName().equals(name)) {
        return layer;
      }
    }
    return null;
  }
}
