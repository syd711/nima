package com.nima.actors;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.*;
import com.nima.components.*;
import com.nima.util.PolygonUtil;
import com.nima.util.Settings;

/**
 * Superclass for spine entities
 */
abstract public class Spine extends Entity implements Updateable {
  protected SpineComponent spineComponent;
  protected MovementComponent movementComponent;
  protected PositionComponent positionComponent;
  protected SpeedComponent speedComponent;
  protected CollisionComponent collisionComponent;
  protected ScalingComponent scalingComponent;
  protected RotationComponent rotationComponent;

  protected BatchTiledMapRenderer renderer;

  protected final TextureAtlas atlas;
  public final Skeleton skeleton;
  protected final AnimationState state;

  protected SkeletonRenderer skeletonRenderer;
  private final SkeletonJson json;
  private String defaultAnimation;

  public Spine(BatchTiledMapRenderer renderer, String path, String defaultAnimation, float jsonScaling) {
    this.renderer = renderer;
    this.defaultAnimation = defaultAnimation;
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

    spineComponent = new SpineComponent();
    add(spineComponent);

    scalingComponent = new ScalingComponent(1f);
    add(scalingComponent);

    positionComponent = new PositionComponent();
    add(positionComponent);

    speedComponent = new SpeedComponent(Settings.MAX_ACTOR_SPEED);
    add(speedComponent);

    rotationComponent = new RotationComponent(this);
    add(rotationComponent);

    collisionComponent = new CollisionComponent();
    add(collisionComponent);

    movementComponent = new MovementComponent(this);
    add(movementComponent);

    //box2d
//    BodyDef def = new BodyDef();
//    def.type = BodyDef.BodyType.DynamicBody;
//    def.fixedRotation = false;
//    def.position.set(targetX, targetY);
//    body = world.createBody(def);
//
//    PolygonShape shape = new PolygonShape();
//    //calculated from center!
//    shape.setAsBox(dimensionComponent.width / 2 / Settings.PPM, dimensionComponent.height / 2 / Settings.PPM);
//    body.createFixture(shape, 1f);
//    shape.dispose();

  }


  @Override
  public void update() {
    state.update(Gdx.graphics.getDeltaTime()); // Update the animation time.
    state.apply(skeleton); // Poses skeleton using current animations. This sets the bones' local SRT.
    skeleton.updateWorldTransform();

    float scaleX = skeleton.getRootBone().getScaleX();
    if(scaleX != scalingComponent.getScaling()) {
      skeleton.getRootBone().setScale(scalingComponent.getScaling());
    }
    skeleton.setPosition(positionComponent.x, positionComponent.y);
    speedComponent.updateSpeed();

    skeletonRenderer.draw(renderer.getBatch(), skeleton); // Draw the skeleton images.
  }

  protected float getHeight() {
    return skeleton.getData().getHeight() * scalingComponent.getScaling();
  }

  public Vector2 getCenter() {
    return PolygonUtil.getSpineCenter(this, "torso");
  }
}
