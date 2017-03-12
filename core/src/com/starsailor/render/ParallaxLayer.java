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
  private final float screenCenterX;
  private final float halfScrollWidthX;
  private final int screenCenterY;
  private final int halfScrollWidthY;

  protected ParallaxLayer(String resource) {
    texture = new Texture(Gdx.files.internal(resource));
    drawable = new TextureRegionDrawable(new TextureRegion(texture));

    screenCenterX = Gdx.graphics.getWidth() / 2;
    screenCenterY = Gdx.graphics.getHeight() / 2;
    halfScrollWidthX = TmxSettings.WORLD_PIXELS_X / 2 - screenCenterX;
    halfScrollWidthY = TmxSettings.WORLD_PIXELS_Y / 2 - screenCenterY;
  }

  public void render(Batch batch) {
    batch.begin();


    float cameraPercentageX = 0;
    float cameraPercentageY = 0;

    //left part X
    if(Game.camera.position.x <= (TmxSettings.WORLD_PIXELS_X / 2)) {
      float percentageOnLeft = (100 - (Game.camera.position.x - screenCenterX) * 100 / halfScrollWidthX) / 100;
      cameraPercentageX = (Game.camera.position.x - screenCenterX * percentageOnLeft) * 100 / TmxSettings.WORLD_PIXELS_X;
    }
    else {
      float percentageOnRight = (Game.camera.position.x - TmxSettings.WORLD_PIXELS_X / 2) * 100 / halfScrollWidthX / 100;
      cameraPercentageX = (Game.camera.position.x + screenCenterX * percentageOnRight) * 100 / TmxSettings.WORLD_PIXELS_X;
    }

    //lower part y
    if(Game.camera.position.y <= (TmxSettings.WORLD_PIXELS_Y / 2)) {
      float percentageOnBottom = (100 - (Game.camera.position.y - screenCenterY) * 100 / halfScrollWidthY) / 100;
      cameraPercentageY = (Game.camera.position.y - screenCenterY * percentageOnBottom) * 100 / TmxSettings.WORLD_PIXELS_Y;
    }
    else {
      float percentageOnTop = (Game.camera.position.y - TmxSettings.WORLD_PIXELS_Y / 2) * 100 / halfScrollWidthY / 100;
      cameraPercentageY = (Game.camera.position.y + screenCenterY * percentageOnTop) * 100 / TmxSettings.WORLD_PIXELS_Y;
    }

    float x = texture.getWidth() * cameraPercentageX / 100;
    float y = texture.getHeight() * cameraPercentageY / 100;
    drawable.draw(batch, x, y, texture.getWidth(), texture.getHeight());

    batch.end();
  }
}
