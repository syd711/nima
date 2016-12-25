package com.nima.actors;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import com.nima.components.*;
import com.nima.util.Resources;
import com.nima.util.Settings;

/**
 * The player with all ashley components.
 */
public class Player extends Spine implements Updateable {

  public Player(World world, RayHandler rayHandler) {
    spineComponent = new SpineComponent(Resources.ACTOR_SPINE, Resources.ACTOR_DEFAULT_ANIMATION, 0.3f);
    add(spineComponent);
    DimensionComponent dimensionComponent = new DimensionComponent(spineComponent);
    add(dimensionComponent);

    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    float targetX = Settings.START_FRAME_X * Settings.FRAME_PIXELS_X + (w / 2);
    float targetY = Settings.START_FRAME_Y * Settings.FRAME_PIXELS_Y + (h / 2) + dimensionComponent.height / 2;
//    targetY = Settings.START_FRAME_Y * Settings.FRAME_PIXELS_Y + (h / 2);

    add(new LightComponent(rayHandler));
    add(new PositionComponent(targetX, targetY));
    add(new ScreenPositionComponent(targetX, targetY));
    add(new SpeedComponent(Settings.MAX_ACTOR_SPEED));
    add(new CollisionComponent(spineComponent));
    add(new MovementComponent(this));

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
  }
}
