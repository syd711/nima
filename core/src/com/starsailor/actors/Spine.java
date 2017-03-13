package com.starsailor.actors;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.*;
import com.starsailor.managers.ResourceManager;
import com.starsailor.model.SpineData;
import com.starsailor.util.SpineUtil;

import static com.starsailor.util.Settings.MPP;

/**
 * Superclass for spine entities
 */
abstract public class Spine extends GameEntity {
  public static final String SPINE_CENTER_SLOT_NAME = "torso";

  public AnimationState state;
  public SkeletonRenderer skeletonRenderer;
  public Skeleton skeleton;
  public float jsonScaling;

  public Spine(SpineData spineData) {
    createSpine(spineData);
  }

  /**
   * The plain spine creation
   */
  private void createSpine(SpineData spineData) {
    this.jsonScaling = spineData.getScale();
    this.skeletonRenderer = new SkeletonRenderer();

    SkeletonData skeletonData = ResourceManager.getInstance().getSkeletonData(spineData.getSpine(), spineData.getScale());
    skeleton = new Skeleton(skeletonData); // Skeleton holds skeleton state (bone positions, slot attachments, etc).

    AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.
    state = new AnimationState(stateData); // Holds the animation state for a skeleton (current animation, time, etc).
    if(spineData.getDefaultAnimation() != null) {
//      state.setAnimation(0, spineData.getDefaultAnimation(), true);
    }
  }

  public float getHeight() {
    return skeleton.getData().getHeight() * jsonScaling;
  }

  public float getWidth() {
    return skeleton.getData().getHeight() * jsonScaling;
  }

  public Vector2 getCenter() {
    Vector2 spineCenter = SpineUtil.getSpineCenter(this, SPINE_CENTER_SLOT_NAME);
    if(spineCenter == null) {
      spineCenter = SpineUtil.getSpineCenter(this, "Schiff_01");
    }
    if(spineCenter == null) {
      throw new UnsupportedOperationException("No center slot found for spine " + this);
    }
    return spineCenter;
  }

  public Vector2 getBox2dCenter() {
    return SpineUtil.getSpineCenter(this, SPINE_CENTER_SLOT_NAME).scl(MPP);
  }

}
