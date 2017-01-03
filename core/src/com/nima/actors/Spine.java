package com.nima.actors;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.*;
import com.nima.components.*;
import com.nima.util.GraphicsUtil;
import com.nima.util.Settings;
import com.nima.util.SpineUtil;

import static com.nima.util.Settings.MPP;

/**
 * Superclass for spine entities
 */
abstract public class Spine extends Entity implements Updateable {
  protected SteerableComponent steerableComponent;
  protected BodyComponent bodyComponent;
  protected SpineComponent spineComponent;
  protected MovementComponent movementComponent;
  protected PositionComponent positionComponent;
  protected SpeedComponent speedComponent;
  protected ScalingComponent scalingComponent;
  protected RotationComponent rotationComponent;

  protected BatchTiledMapRenderer renderer;

  protected final TextureAtlas atlas;
  protected final AnimationState state;
  protected SkeletonRenderer skeletonRenderer;

  public final Skeleton skeleton;

  private final SkeletonJson json;
  private final float jsonScaling;

  public Spine(BatchTiledMapRenderer renderer, String path, String defaultAnimation, float jsonScaling) {
    this(renderer, path, defaultAnimation, jsonScaling, -1f, -1f);
  }

  public Spine(BatchTiledMapRenderer renderer, String path, String defaultAnimation, float jsonScaling, float x, float y) {
    this.renderer = renderer;
    this.jsonScaling = jsonScaling;
    this.skeletonRenderer = new SkeletonRenderer();

    atlas = new TextureAtlas(Gdx.files.internal(path + ".atlas"));
    // This loads skeleton JSON data, which is stateless.
    json = new SkeletonJson(atlas);
    json.setScale(jsonScaling); // Load the skeleton at x% the size it was in Spine.
    SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(path + ".json"));

    skeleton = new Skeleton(skeletonData); // Skeleton holds skeleton state (bone positions, slot attachments, etc).

    AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.
    state = new AnimationState(stateData); // Holds the animation state for a skeleton (current animation, time, etc).
//    state.setAnimation(0, defaultAnimation, true);

    scalingComponent = new ScalingComponent(1f);
    add(scalingComponent);

    positionComponent = new PositionComponent();
    if(x == -1 || y == -1) {
      Vector2 screenCenter = GraphicsUtil.getScreenCenter(getHeight());
      positionComponent.x = screenCenter.x;
      positionComponent.y = screenCenter.y;
    }
    add(positionComponent);

    spineComponent = new SpineComponent();
    add(spineComponent);

    speedComponent = new SpeedComponent(Settings.MAX_ACTOR_SPEED);
    add(speedComponent);

    rotationComponent = new RotationComponent(this);
    add(rotationComponent);

    movementComponent = new MovementComponent(this);
    add(movementComponent);

    bodyComponent = new BodyComponent(this);
    add(bodyComponent);

    steerableComponent = new SteerableComponent(bodyComponent.body);
    add(steerableComponent);
  }

  public float getJsonScaling() {
    return jsonScaling;
  }

  @Override
  public void update() {
    renderSpine();

    //box2d rendering
    PositionComponent pos = getComponent(PositionComponent.class);
    bodyComponent.body.setTransform(getCenter().x*MPP, getCenter().y*MPP, rotationComponent.getB2dAngle());
  }

  /**
   * Renders the Spine using the map batch
   */
  protected void renderSpine() {
    //spine rendering
    state.update(Gdx.graphics.getDeltaTime()); // Update the animation time.
    state.apply(skeleton); // Poses skeleton using current animations. This sets the bones' local SRT.
    skeleton.updateWorldTransform();


    skeleton.setPosition(positionComponent.x, positionComponent.y);
    speedComponent.updateValue();

    skeletonRenderer.draw(renderer.getBatch(), skeleton); // Draw the skeleton images.
  }

  protected float getHeight() {
    return skeleton.getData().getHeight() * scalingComponent.getCurrentValue();
  }

  public Vector2 getCenter() {
    return SpineUtil.getSpineCenter(this, "torso");
  }
}
