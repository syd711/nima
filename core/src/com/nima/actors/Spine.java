package com.nima.actors;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.*;
import com.nima.components.*;
import com.nima.util.GraphicsUtil;
import com.nima.util.PolygonUtil;
import com.nima.util.Settings;

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
  protected CollisionComponent collisionComponent;
  protected ScalingComponent scalingComponent;
  protected RotationComponent rotationComponent;

  protected BatchTiledMapRenderer renderer;

  protected final TextureAtlas atlas;
  protected final AnimationState state;
  protected SkeletonRenderer skeletonRenderer;

  public final Skeleton skeleton;

  private final SkeletonJson json;

  public Spine(BatchTiledMapRenderer renderer, String path, String defaultAnimation, float jsonScaling) {
    this(renderer, path, defaultAnimation, jsonScaling, -1f, -1f);
  }

  public Spine(BatchTiledMapRenderer renderer, String path, String defaultAnimation, float jsonScaling, float x, float y) {
    this.renderer = renderer;
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

    positionComponent = new PositionComponent();
    add(positionComponent);

    spineComponent = new SpineComponent();
    add(spineComponent);

    scalingComponent = new ScalingComponent(1f);
    add(scalingComponent);

    speedComponent = new SpeedComponent(Settings.MAX_ACTOR_SPEED);
    add(speedComponent);

    rotationComponent = new RotationComponent(this);
    add(rotationComponent);

    collisionComponent = new CollisionComponent();
    add(collisionComponent);

    movementComponent = new MovementComponent(this);
    add(movementComponent);

    bodyComponent = new BodyComponent(this, x, y);
    add(bodyComponent);

    steerableComponent = new SteerableComponent(bodyComponent.body);
    add(steerableComponent);

    if(x == -1 || y == -1) {
      Vector2 screenCenter = GraphicsUtil.getScreenCenter(getHeight());
      positionComponent.x = screenCenter.x;
      positionComponent.y = screenCenter.y;
    }
  }


  @Override
  public void update() {
    state.update(Gdx.graphics.getDeltaTime()); // Update the animation time.
    state.apply(skeleton); // Poses skeleton using current animations. This sets the bones' local SRT.
    skeleton.updateWorldTransform();

    float scaleX = skeleton.getRootBone().getScaleX();
    if(scaleX != scalingComponent.getCurrentValue()) {
      skeleton.getRootBone().setScale(scalingComponent.getCurrentValue());
    }
    skeleton.setPosition(positionComponent.x, positionComponent.y);
    speedComponent.updateValue();

    skeletonRenderer.draw(renderer.getBatch(), skeleton); // Draw the skeleton images.
  }

  protected float getHeight() {
    return skeleton.getData().getHeight() * scalingComponent.getCurrentValue();
  }

  public Vector2 getCenter() {
    return PolygonUtil.getSpineCenter(this, "torso");
  }
}
