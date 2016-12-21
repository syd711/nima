package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.esotericsoftware.spine.*;
import com.nima.util.Settings;

/**
 * Component implementation for Spines.
 */
public class SpineComponent implements Component {
  protected final TextureAtlas atlas;
  protected final Skeleton skeleton;
  protected final AnimationState state;

  protected SkeletonRenderer skeletonRenderer;
  private final SkeletonJson json;
  private String defaultAnimation;

  private float targetAngle = 0;

  public SpineComponent(String spineName, String defaultAnimation, float scale) {
    this.defaultAnimation = defaultAnimation;
    skeletonRenderer = new SkeletonRenderer();

    atlas = new TextureAtlas(Gdx.files.internal(spineName + ".atlas"));
    // This loads skeleton JSON data, which is stateless.
    json = new SkeletonJson(atlas);
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
    skeleton.updateWorldTransform();

    float currentAngle = skeleton.getRootBone().getRootRotation();
    if(currentAngle < targetAngle) {
      float newAngle = currentAngle + Settings.ACTOR_ROTATION_SPEED;
      skeleton.getRootBone().setRootRotation(newAngle);
    }

    if(currentAngle > targetAngle) {
      float newAngle = currentAngle - Settings.ACTOR_ROTATION_SPEED;
      skeleton.getRootBone().setRootRotation(newAngle);
    }

    skeletonRenderer.draw(renderer.getBatch(), skeleton); // Draw the skeleton images.
  }

  public float getRotation() {
    return skeleton.getRootBone().getWorldRotationX();
  }

  public void setRotation(float angle) {
    this.targetAngle = Math.round(angle * -1);

  }

  public float getScaling() {
    return json.getScale();
  }


}
