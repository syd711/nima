package com.starsailor.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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

  private TiledMultiMapRenderer renderer;

  protected ParallaxLayer(TiledMultiMapRenderer renderer, FileHandle resource) {
    this.renderer = renderer;

    texture = new Texture(resource);
    drawable = new TextureRegionDrawable(new TextureRegion(texture));

    screenCenterX = Gdx.graphics.getWidth() / 2;
    screenCenterY = Gdx.graphics.getHeight() / 2;
    halfScrollWidthX = renderer.getWorldPixelsX() / 2 - screenCenterX;
    halfScrollWidthY = renderer.getWorldPixelsY() / 2 - screenCenterY;
  }

  public void render(Batch batch) {
    batch.begin();


    float cameraPercentageX = 0;
    float cameraPercentageY = 0;

    //left part X
    if(Game.camera.position.x <= (renderer.getWorldPixelsX() / 2)) {
      float percentageOnLeft = (100 - (Game.camera.position.x - screenCenterX) * 100 / halfScrollWidthX) / 100;
      cameraPercentageX = (Game.camera.position.x - screenCenterX * percentageOnLeft) * 100 / renderer.getWorldPixelsX();
    }
    else {
      float percentageOnRight = (Game.camera.position.x - renderer.getWorldPixelsX() / 2) * 100 / halfScrollWidthX / 100;
      cameraPercentageX = (Game.camera.position.x + screenCenterX * percentageOnRight) * 100 / renderer.getWorldPixelsX();
    }

    //lower part y
    if(Game.camera.position.y <= (renderer.getWorldPixelsY() / 2)) {
      float percentageOnBottom = (100 - (Game.camera.position.y - screenCenterY) * 100 / halfScrollWidthY) / 100;
      cameraPercentageY = (Game.camera.position.y - screenCenterY * percentageOnBottom) * 100 / renderer.getWorldPixelsY();
    }
    else {
      float percentageOnTop = (Game.camera.position.y - renderer.getWorldPixelsY() / 2) * 100 / halfScrollWidthY / 100;
      cameraPercentageY = (Game.camera.position.y + screenCenterY * percentageOnTop) * 100 / renderer.getWorldPixelsY();
    }

    float x = texture.getWidth() * cameraPercentageX / 100;
    float y = texture.getHeight() * cameraPercentageY / 100;
    drawable.draw(batch, x, y, texture.getWidth(), texture.getHeight());

    batch.end();
  }
}
