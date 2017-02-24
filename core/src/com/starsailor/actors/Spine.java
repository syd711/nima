package com.starsailor.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.*;
import com.starsailor.data.DataEntities;
import com.starsailor.data.SpineData;
import com.starsailor.util.SpineUtil;

import static com.starsailor.util.Settings.MPP;

/**
 * Superclass for spine entities
 */
abstract public class Spine extends GameEntity {
  public AnimationState state;
  public SkeletonRenderer skeletonRenderer;
  public Skeleton skeleton;
  public float jsonScaling;

  public Spine(String path, SpineData spineData) {
    createSpine(path, spineData);
  }

  /**
   * The plain spine creation
   */
  private void createSpine(String path, SpineData spineData) {
    this.jsonScaling = spineData.getScale();
    this.skeletonRenderer = new SkeletonRenderer();

    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(path + ".atlas"));
    // This loads skeleton JSON data, which is stateless.
    SkeletonJson json = new SkeletonJson(atlas);
    json.setScale(jsonScaling); // Load the skeleton at x% the size it was in Spine.
    SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(path + ".json"));

    skeleton = new Skeleton(skeletonData); // Skeleton holds skeleton state (bone positions, slot attachments, etc).

    AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.
    state = new AnimationState(stateData); // Holds the animation state for a skeleton (current animation, time, etc).
    if(spineData.getDefaultAnimation() != null) {
      state.setAnimation(0, spineData.getDefaultAnimation(), true);
    }
  }

  public float getHeight() {
    return skeleton.getData().getHeight() * jsonScaling;
  }

  public float getWidth() {
    return skeleton.getData().getHeight() * jsonScaling;
  }

  public Vector2 getCenter() {
    Vector2 spineCenter = SpineUtil.getSpineCenter(this, DataEntities.SPINE_CENTER_SLOT_NAME);
    if(spineCenter == null) {
      throw new UnsupportedOperationException("No center slot found for spine " + this);
    }
    return spineCenter;
  }

  public Vector2 getBox2dCenter() {
    return SpineUtil.getSpineCenter(this, DataEntities.SPINE_CENTER_SLOT_NAME).scl(MPP);
  }

}
