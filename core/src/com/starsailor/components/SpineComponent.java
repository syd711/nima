package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.esotericsoftware.spine.*;
import com.esotericsoftware.spine.attachments.Attachment;
import com.esotericsoftware.spine.attachments.RegionAttachment;
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
  private float rotation;

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

  public Vector2 getCenter() {
    Vector2 spineCenter = getSpineCenter("torso");
    if(spineCenter == null) {
      spineCenter = getSpineCenter("Schiff_01"); //TODO
    }
    if(spineCenter == null) {
      throw new UnsupportedOperationException("No center slot found for spine " + this);
    }
    return spineCenter;
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
    skeletonRenderer.draw(batch, getSkeleton()); // Draw the skeleton images.
  }

  @Override
  public void reset() {
    animationState = null;
    skeletonRenderer = null;
    skeleton = null;
  }

  //-------------- Helper -------------------------------------

  /**
   * Returns the center of the spine by search for
   * a slot with the given name and returning it's first position vertice.
   */
  private Vector2 getSpineCenter(String slotName) {
    Array<Slot> drawOrder = skeleton.getDrawOrder();
    boolean premultipliedAlpha = false;
    for(int i = 0, n = drawOrder.size; i < n; i++) {
      Slot slot = drawOrder.get(i);
      Attachment attachment = slot.getAttachment();
      if(attachment instanceof RegionAttachment) {
        RegionAttachment regionAttachment = (RegionAttachment) attachment;
        float[] vertices = regionAttachment.updateWorldVertices(slot, premultipliedAlpha);
        //TODO not exact enough
        String name = slot.getData().getName();
        if(slotName.equals(name)) {
          return new Vector2(vertices[0], vertices[1]);
        }
      }
    }
    return null;
  }
}
