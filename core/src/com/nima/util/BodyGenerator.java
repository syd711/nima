package com.nima.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.nima.Game;
import com.nima.actors.Player;
import com.nima.actors.RoutePoint;
import com.nima.actors.Spine;

import static com.nima.util.Settings.MPP;


public class BodyGenerator {
  private final static short PLAYER_BITS = 0x0001;
  private final static short NPC_BITS = 0x0002;
  private final static short WORLD_BITS = 0x0004;
//  public final static short BITS = 0x0008;
//  public final static short BITS = 0x0016;
//  public final static short BITS = 0x0032;

  private final static short MASK_PLAYER = NPC_BITS | WORLD_BITS;
  private final static short MASK_NPC = PLAYER_BITS | NPC_BITS;
  private final static short MASK_WORLD = PLAYER_BITS;

  private static World world = Game.world;

  /**
   * Creates the Box2d body for the given spine
   */
  public static Body createSpineBody(World world, Spine spine) {
    Vector2 center = spine.getCenter();

    BodyDef bdef = new BodyDef();
    bdef.type = BodyDef.BodyType.DynamicBody;
    bdef.position.set(center.x * MPP, center.y * MPP);
    Body b = world.createBody(bdef);

    PolygonShape shape = new PolygonShape();
    float scaling = spine.jsonScaling;
    shape.setAsBox(spine.skeleton.getData().getWidth()*scaling/2 * MPP, spine.skeleton.getData().getHeight()*scaling/2 * MPP);

    FixtureDef fdef = new FixtureDef();
    fdef.density = 1;
    fdef.restitution = 0.9f;
    fdef.shape = shape;
    fdef.filter.groupIndex = 0;
    if(spine instanceof Player) {
      fdef.filter.categoryBits = PLAYER_BITS;
      fdef.filter.maskBits = MASK_PLAYER;
    }
    else {
      fdef.filter.categoryBits = NPC_BITS;
      fdef.filter.maskBits = MASK_NPC;
    }

    b.createFixture(fdef);
    shape.dispose();
    return b;
  }

  public static Body createMapObjectBody(Shape shape) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.StaticBody;
    Body body = world.createBody(bodyDef);

    FixtureDef fdef = new FixtureDef();
    fdef.shape = shape;
    fdef.isSensor = true;
    fdef.filter.groupIndex = 0;
    fdef.filter.categoryBits = WORLD_BITS;
    fdef.filter.maskBits = MASK_WORLD;
    body.createFixture(fdef);

    shape.dispose();
    return body;
  }

  public static Body createBulletBody(Vector2 position) {
    BodyDef bdef = new BodyDef();
    bdef.type = BodyDef.BodyType.DynamicBody;
    bdef.position.set(position.x * MPP, position.y * MPP);
    Body b = world.createBody(bdef);

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(10 * MPP, 2.5f * MPP);

    FixtureDef fdef = new FixtureDef();
    fdef.isSensor = true;
    fdef.density = 0.02f;
    fdef.restitution = 0.9f;
    fdef.shape = shape;
    b.createFixture(fdef);
    shape.dispose();

    return b;
  }

  public static Body generateRoutePointBody(RoutePoint point) {
    BodyDef bdef = new BodyDef();
    bdef.type = BodyDef.BodyType.DynamicBody;
    bdef.position.set(point.position.x * MPP, point.position.y * MPP);
    Body b = world.createBody(bdef);

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(10 * MPP, 10 * MPP);

    FixtureDef fdef = new FixtureDef();
    fdef.isSensor = true;
    fdef.density = 1;
    fdef.restitution = 0.9f;
    fdef.shape = shape;
    b.createFixture(fdef);
    shape.dispose();

    return b;
  }
}
