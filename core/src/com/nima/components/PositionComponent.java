package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.nima.util.Settings;

/**
 *
 */
public class PositionComponent implements Component {
  public float x = 0.0f;
  public float y = 0.0f;

  public void translate(float x, float y) {
    this.x+=x;
    this.y+=y;
  }

  public void center() {
    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    x = Settings.START_FRAME_X * Settings.FRAME_PIXELS_X + (w / 2);
    y = Settings.START_FRAME_Y * Settings.FRAME_PIXELS_Y + (h / 2);
  }
}
