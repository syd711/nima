package com.nima.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.esotericsoftware.spine.*;
import com.nima.render.ActorBasedTiledMultiMapRenderer;

/**
 * Default renderer for spine animations
 */
public class SpineActor extends Actor {
  protected final TextureAtlas atlas;
  protected final Skeleton skeleton;
  protected final AnimationState state;

  protected SkeletonRenderer skeletonRenderer;

  protected float height;
  protected float width;

  public SpineActor(ActorBasedTiledMultiMapRenderer renderer, String spineName, String defaultAnimation, float scale, int x, int y) {
    super(renderer);
    skeletonRenderer = new SkeletonRenderer();

    atlas = new TextureAtlas(Gdx.files.internal(spineName+ ".atlas"));
    SkeletonJson json = new SkeletonJson(atlas); // This loads skeleton JSON data, which is stateless.
    json.setScale(scale); // Load the skeleton at x% the size it was in Spine.
    SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(spineName + ".json"));

    skeleton = new Skeleton(skeletonData); // Skeleton holds skeleton state (bone positions, slot attachments, etc).

    AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.
    state = new AnimationState(stateData); // Holds the animation state for a skeleton (current animation, time, etc).
    state.setAnimation(0, defaultAnimation, true);

    height = skeleton.getData().getHeight() * scale;
    width =  skeleton.getData().getWidth() * scale;

    skeleton.setPosition(x, y);
  }

  @Override
  public boolean moveBy(int x, int y) {
    return false;
  }

  @Override
  public void render() {
    state.update(Gdx.graphics.getDeltaTime()); // Update the animation time.

    state.apply(skeleton); // Poses skeleton using current animations. This sets the bones' local SRT.
    skeleton.updateWorldTransform(); // Uses the bones' local SRT to compute their world SRT.

    skeletonRenderer.draw(renderer.getBatch(), skeleton); // Draw the skeleton images.
  }

  @Override
  public float getX() {
    return skeleton.getX();
  }

  @Override
  public float getY() {
    return skeleton.getY();
  }

  @Override
  public void setPosition(float x, float y) {
    skeleton.setPosition(x, y);
  }

  @Override
  public boolean translate(int x, int y) {
    setPosition(getX()+x, getY()+y);
    return true;
  }

  @Override
  public boolean intersects(MapObject mapObject) {
    return false;
  }
}
