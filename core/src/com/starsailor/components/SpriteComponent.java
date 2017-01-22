package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;


public class SpriteComponent implements Component, Poolable {
  public Sprite sprite;
  private float angle;

  public void setTextures(Texture texture) {
    sprite = new Sprite(texture);
  }

  @Override
  public void reset() {
    sprite = null;
  }

  public void setSelectionMarkerAt(Vector2 pos) {
    sprite.setPosition(pos.x-sprite.getWidth()/2, pos.y-sprite.getHeight()/2);
  }

  public void setAngle(float angle) {
    sprite.setRotation(angle);
  }
}
