package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Pool.Poolable;


public class SpriteComponent implements Component, Poolable {
  public Sprite sprite;

  public void setTextures(Texture texture) {
    sprite = new Sprite(texture);
  }

  @Override
  public void reset() {
    sprite = null;
  }
}
