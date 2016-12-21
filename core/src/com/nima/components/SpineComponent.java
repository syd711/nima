package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.esotericsoftware.spine.*;

/**
 * Component implementation for Spines.
 */
public class SpineComponent implements Component {
  protected final TextureAtlas atlas;
  protected final Skeleton skeleton;
  protected final AnimationState state;
  private float scaling = 0;

  protected SkeletonRenderer skeletonRenderer;

  public SpineComponent(String spineName, String defaultAnimation, float scale) {
    this.scaling = scale;
    skeletonRenderer = new SkeletonRenderer();

    atlas = new TextureAtlas(Gdx.files.internal(spineName+ ".atlas"));
    SkeletonJson json = new SkeletonJson(atlas); // This loads skeleton JSON data, which is stateless.
    json.setScale(scale); // Load the skeleton at x% the size it was in Spine.
    SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(spineName + ".json"));

    skeleton = new Skeleton(skeletonData); // Skeleton holds skeleton state (bone positions, slot attachments, etc).

    AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.
    state = new AnimationState(stateData); // Holds the animation state for a skeleton (current animation, time, etc).
    state.setAnimation(0, defaultAnimation, true);
  }

  public void setPosition(float x, float y) {
    skeleton.setPosition(x, y);
  }

  public void render(BatchTiledMapRenderer renderer) {
    state.update(Gdx.graphics.getDeltaTime()); // Update the animation time.

    state.apply(skeleton); // Poses skeleton using current animations. This sets the bones' local SRT.
    skeleton.updateWorldTransform(); // Uses the bones' local SRT to compute their world SRT.

    skeletonRenderer.draw(renderer.getBatch(), skeleton); // Draw the skeleton images.
  }

  public float getRotation() {
    return 0;
  }

  public float getScaling() {
    return scaling;
  }
}
