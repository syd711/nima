package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Pool;
import com.esotericsoftware.spine.*;
import com.starsailor.actors.SpineShipAnimations;
import com.starsailor.managers.ResourceManager;
import com.starsailor.model.SpineData;

/**
 * Component implementation for Spines.
 */
public class SpineComponent implements Component, Pool.Poolable {
  private AnimationState animationState;
  private SkeletonRenderer skeletonRenderer;
  private Skeleton skeleton;
  private float jsonScaling;
  private boolean enabled = true;

  public SpineComponent(SpineData spineData) {
    this.jsonScaling = spineData.getScale();
    this.skeletonRenderer = new SkeletonRenderer();

    SkeletonData skeletonData = ResourceManager.getInstance().getSkeletonData(spineData.getSpine(), spineData.getScale());
    skeleton = new Skeleton(skeletonData); // Skeleton holds skeleton animationState (bone positions, slot attachments, etc).

    AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.
    animationState = new AnimationState(stateData); // Holds the animation animationState for a skeleton (current animation, time, etc).
    if(spineData.getDefaultAnimation() != null) {
      animationState.setAnimation(0, spineData.getDefaultAnimation(), true);
    }
  }

  public void update(float deltaTime) {
    getAnimationState().update(deltaTime); // Update the animation time.
  }

  public AnimationState getAnimationState() {
    return animationState;
  }

  public void setAnimation(SpineShipAnimations animation) {
    animationState.setAnimation(0, animation.toString(), true);
  }

  public float getJsonScaling() {
    return jsonScaling;
  }

  public Skeleton getSkeleton() {
    return skeleton;
  }

  public float getHeight() {
    return skeleton.getData().getHeight() * jsonScaling;
  }

  public float getWidth() {
    return skeleton.getData().getHeight() * jsonScaling;
  }

  public void setRotation(float rotation) {
    getSkeleton().getRootBone().setRootRotation(rotation);
  }

  public void setPosition(float x, float y) {
    skeleton.setPosition(x, y);
  }

  public void render(Batch batch) {
    //apply the rendering to the spine engine
    animationState.apply(getSkeleton()); // Poses skeleton using current animations. This sets the bones' local SRT.
    skeleton.updateWorldTransform();
    //skeleton.setColor(new Color(1,1,1, 0.2f)); opacity
    skeletonRenderer.draw(batch, getSkeleton()); // Draw the skeleton images.
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public void reset() {
    animationState = null;
    skeletonRenderer = null;
    skeleton = null;
  }
}
