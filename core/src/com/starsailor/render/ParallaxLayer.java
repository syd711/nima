package com.starsailor.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.starsailor.Game;

/**
 * Rendering of a parallax layer.
 */
public class ParallaxLayer {

  private final Texture texture;
  private final TextureRegionDrawable drawable;

  protected ParallaxLayer(String resource) {
    texture = new Texture(Gdx.files.internal(resource));
    drawable = new TextureRegionDrawable(new TextureRegion(texture));
  }

  public void render(Batch batch) {
    batch.begin();

    float offset = (TmxSettings.FRAME_PIXELS_X * 1f) / texture.getWidth();

    float x = Game.camera.position.x*offset - (TmxSettings.FRAME_PIXELS_X/2*offset);
    float y = Game.camera.position.y*offset - (TmxSettings.FRAME_PIXELS_Y/2*offset);

    drawable.draw(batch, x, y, texture.getWidth(), texture.getHeight());

    batch.end();
  }
}
