package com.nima.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.*;
import com.nima.components.*;
import com.nima.util.SpineUtil;

/**
 * Superclass for spine entities
 */
abstract public class Spine extends BodyEntity implements Updateable {
  protected SteerableComponent steerableComponent;
  protected SpineComponent spineComponent;

  protected SpeedComponent speedComponent;
  protected ScalingComponent scalingComponent;
  protected RotationComponent rotationComponent;

  public final AnimationState state;
  public SkeletonRenderer skeletonRenderer;
  public final Skeleton skeleton;
  public final float jsonScaling;

  public Spine(String path, String defaultAnimation, float jsonScaling) {
    this(path, defaultAnimation, jsonScaling, -1f, -1f);
  }

  public Spine(String path, String defaultAnimation, float jsonScaling, float x, float y) {
    this.jsonScaling = jsonScaling;
    this.skeletonRenderer = new SkeletonRenderer();

    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(path + ".atlas"));
    // This loads skeleton JSON data, which is stateless.
    SkeletonJson json = new SkeletonJson(atlas);
    json.setScale(jsonScaling); // Load the skeleton at x% the size it was in Spine.
    SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(path + ".json"));

    skeleton = new Skeleton(skeletonData); // Skeleton holds skeleton state (bone positions, slot attachments, etc).

    AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.
    state = new AnimationState(stateData); // Holds the animation state for a skeleton (current animation, time, etc).
//    state.setAnimation(0, defaultAnimation, true);

    scalingComponent = ComponentFactory.addScalingComponent(this);
    positionComponent = ComponentFactory.addPositionComponent(this, x == -1 || y == -1, getHeight());
    spineComponent = ComponentFactory.addSpineComponent(this);
    speedComponent = ComponentFactory.addSpeedComponent(this);
    rotationComponent = ComponentFactory.addRotationComponent(this);
    bodyComponent = ComponentFactory.addBodyComponent(this);

    steerableComponent = new SteerableComponent(bodyComponent.body);
    add(steerableComponent);
  }

  @Override
  public void update() {
  }

  protected float getHeight() {
    return skeleton.getData().getHeight() * scalingComponent.getCurrentValue();
  }

  public Vector2 getCenter() {
    return SpineUtil.getSpineCenter(this, "torso");
  }
}
