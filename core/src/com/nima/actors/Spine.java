package com.nima.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.*;
import com.nima.data.DataEntities;
import com.nima.util.SpineUtil;

/**
 * Superclass for spine entities
 */
abstract public class Spine extends BodyEntity {

  public AnimationState state;
  public SkeletonRenderer skeletonRenderer;
  public Skeleton skeleton;
  public float jsonScaling;

  public Spine(String path, String defaultAnimation, float scaling) {
    createSpine(path, defaultAnimation, scaling);
  }

  /**
   * The plain spine creation
   */
  private void createSpine(String path, String defaultAnimation, float scaling) {
    this.jsonScaling = scaling;
    this.skeletonRenderer = new SkeletonRenderer();

    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(path + ".atlas"));
    // This loads skeleton JSON data, which is stateless.
    SkeletonJson json = new SkeletonJson(atlas);
    json.setScale(jsonScaling); // Load the skeleton at x% the size it was in Spine.
    SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(path + ".json"));

    skeleton = new Skeleton(skeletonData); // Skeleton holds skeleton state (bone positions, slot attachments, etc).

    AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.
    state = new AnimationState(stateData); // Holds the animation state for a skeleton (current animation, time, etc).
    if(defaultAnimation != null) {
      state.setAnimation(0, defaultAnimation, true);
    }
  }

  protected float getHeight() {
    return skeleton.getData().getHeight() * jsonScaling;
  }

  public Vector2 getCenter() {
    return SpineUtil.getSpineCenter(this, DataEntities.SPINE_CENTER_SLOT_NAME);
  }
}
