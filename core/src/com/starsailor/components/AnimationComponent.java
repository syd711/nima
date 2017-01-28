package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

import java.util.ArrayList;
import java.util.List;

/**
 * Component for animations
 */
public class AnimationComponent implements Component, Pool.Poolable {
  public List<Texture> textures = new ArrayList<>();
  private SpriteAnimation animation;
  private Sprite sprite;
  private float width;

  @Override
  public void reset() {
    sprite = null;
    animation = null;
    textures.clear();
  }

  public void setWrappedRepeat() {
    for(Texture texture : textures) {
      texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }
  }

  public void setRotation(float rotation) {
    getSprite().setRotation(rotation);
  }

  public void setPosition(Vector2 position) {
    getSprite().setPosition(position.x, position.y);
  }

  private Sprite getSprite() {
    if(sprite == null) {
      sprite = new Sprite(textures.get(0));
    }
    return sprite;
  }

  public void setWidth(float width) {
    getSprite().setSize(width, getSprite().getHeight());
  }

  public void draw(Batch batch, float deltaTime) {
    getAnimation().draw(batch, deltaTime);
  }

  private SpriteAnimation getAnimation() {
    if(animation == null) {
      List<TextureRegion> regions = new ArrayList<>();
      for(Texture texture : textures) {
        TextureRegion textureRegion = new TextureRegion(texture, (int) getSprite().getWidth(), (int) getSprite().getHeight());
        regions.add(textureRegion);
        animation = new SpriteAnimation(0.2f, regions.toArray(new TextureRegion[0]));
        animation.setPlayMode(Animation.PlayMode.LOOP);
      }
    }

    return animation;
  }

  /**
   * The actual animation created from all component data.
   */
  class SpriteAnimation extends Animation {
    private float stateTime = 0;

    public SpriteAnimation(float frameDuration, TextureRegion... keyFrames) {
      super(frameDuration, keyFrames);
    }

    public void draw(Batch batch, float deltaTime) {
      stateTime += deltaTime;

      TextureRegion region = getKeyFrame(stateTime);
      batch.draw(region, sprite.getX(), sprite.getY(),
          sprite.getOriginX(), sprite.getOriginY(),
          sprite.getWidth(), sprite.getHeight(),
          1, 1,
          sprite.getRotation());
    }
  }
}
