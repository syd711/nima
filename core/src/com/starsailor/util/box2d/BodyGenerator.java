package com.starsailor.util.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.starsailor.Game;
import com.starsailor.actors.Player;
import com.starsailor.actors.Ship;
import com.starsailor.components.SpineShipComponent;
import com.starsailor.model.BodyData;
import com.starsailor.model.WeaponData;
import com.starsailor.render.TiledMultiMapRenderer;
import com.starsailor.util.Settings;

import static com.starsailor.util.Settings.MPP;


public class BodyGenerator {
  private final static short PLAYER_BITS = 0x0001;
  private final static short NPC_BITS = 0x0002;
  private final static short WORLD_BITS = 0x0004;
  private final static short BULLET_BITS = 0x0008;
//  public final static short BITS = 0x0032;

  private final static short MASK_PLAYER = NPC_BITS | WORLD_BITS | BULLET_BITS;
  private final static short MASK_NPC = PLAYER_BITS | NPC_BITS | BULLET_BITS;
  private final static short MASK_WORLD = PLAYER_BITS;
  private final static short MASK_BULLET_BITS = BULLET_BITS | PLAYER_BITS | NPC_BITS;

  private static World world = Game.world;

  public static Body create(BodyData bodyData, Vector2 worldPosition) {
    BodyDef bdef = new BodyDef();
    bdef.type = BodyDef.BodyType.DynamicBody;
    bdef.position.set(Box2dUtil.toBox2Vector(worldPosition));
    Body b = world.createBody(bdef);

    FixtureDef fdef = new FixtureDef();
    fdef.filter.categoryBits = WORLD_BITS;
    fdef.filter.maskBits = MASK_WORLD;

    Shape shape;
    if(bodyData.getRadius() > 0) {
      shape = new CircleShape();
      shape.setRadius(bodyData.getRadius() * MPP);
      fdef.shape = shape;
    }
    else {
      shape = new PolygonShape();
      ((PolygonShape) shape).setAsBox(bodyData.getWidth() * MPP, bodyData.getHeight() * MPP);
      fdef.shape = shape;
    }

    fdef.isSensor = bodyData.isSensor();
    fdef.density = bodyData.getDensity();
    fdef.restitution = 0.1f;
    fdef.shape = shape;
    b.createFixture(fdef);

    shape.dispose();
    return b;
  }

  public static Body createLootBody(Vector2 worldPosition) {
    return createClickBody(worldPosition);
  }

  public static Body createClickBody(Vector2 worldPosition) {
    BodyDef bdef = new BodyDef();
    bdef.type = BodyDef.BodyType.DynamicBody;
    bdef.position.set(Box2dUtil.toBox2Vector(worldPosition));
    Body b = world.createBody(bdef);

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(30 * MPP, 30 * MPP);

    FixtureDef fdef = new FixtureDef();
    fdef.isSensor = true;
    fdef.filter.categoryBits = WORLD_BITS;
    fdef.filter.maskBits = MASK_WORLD;
    fdef.density = 1;
    fdef.restitution = 0.1f;
    fdef.shape = shape;
    b.createFixture(fdef);

    shape.dispose();
    return b;
  }

  /**
   * Creates the Box2d body for the given spine
   */
  public static Body createSpineBody(World world, Ship ship, BodyData bodyData, Vector2 position) {
    SpineShipComponent spineComponent = ship.getComponent(SpineShipComponent.class);
    float scaling = spineComponent.getJsonScaling() - 0.02f; //TODO better body

    Shape shape;
    if(bodyData.getRadius() > 0) {
      shape = new CircleShape();
      shape.setRadius(bodyData.getRadius() * scaling * MPP);
    }
    else {
      shape = new PolygonShape();
      ((PolygonShape) shape).setAsBox(spineComponent.getSkeleton().getData().getWidth() * scaling / 2 * MPP,
          spineComponent.getSkeleton().getData().getHeight() * scaling / 2 * MPP);
    }
    return spineBody(shape, world, bodyData, ship, position, bodyData.isSensor());
  }

  private static Body spineBody(Shape shape, World world, BodyData bodyData, Ship ship, Vector2 center, boolean sensor) {
    BodyDef bdef = new BodyDef();
    bdef.type = BodyDef.BodyType.DynamicBody;
    bdef.position.set(center.x * MPP, center.y * MPP);
    Body b = world.createBody(bdef);

    if(bodyData.getLinearDamping() > 0) {
      b.setLinearDamping(bodyData.getLinearDamping());
    }
    else {
      b.setLinearDamping(4f);
    }

    if(bodyData.getAngularDamping() > 0) {
      b.setAngularDamping(bodyData.getAngularDamping());
    }


    FixtureDef fdef = new FixtureDef();
    fdef.density = 1;
    if(bodyData.getDensity() > 0) {
      fdef.density = bodyData.getDensity();
    }
    fdef.isSensor = sensor;
    fdef.restitution = 1f;
    fdef.shape = shape;
    fdef.filter.groupIndex = 0;
    if(ship instanceof Player) {
      fdef.filter.categoryBits = PLAYER_BITS;
      fdef.filter.maskBits = MASK_PLAYER;
    }
    else {
      fdef.filter.categoryBits = NPC_BITS;
      fdef.filter.maskBits = MASK_NPC;
    }

    b.createFixture(fdef);
    b.resetMassData();

    shape.dispose();
    return b;
  }

  public static Body createBulletBody(Vector2 position, WeaponData weaponData, boolean friendly) {
    BodyData bodyData = weaponData.getBodyData();

    BodyDef bdef = new BodyDef();
    bdef.type = BodyDef.BodyType.DynamicBody;
    bdef.bullet = true;
    bdef.position.set(position.x * MPP, position.y * MPP);
    Body b = world.createBody(bdef);
    b.setLinearDamping(bodyData.getLinearDamping());
    b.setAngularDamping(bodyData.getAngularDamping());

    FixtureDef fdef = new FixtureDef();

    Shape shape;
    if(bodyData.getRadius() > 0) {
      shape = new CircleShape();
      shape.setRadius(bodyData.getRadius() * MPP);
      fdef.shape = shape;
    }
    else {
      shape = new PolygonShape();
      ((PolygonShape) shape).setAsBox(bodyData.getWidth() * MPP, bodyData.getHeight() * MPP);
      fdef.shape = shape;
    }

    fdef.filter.groupIndex = 0;
    fdef.isSensor = bodyData.isSensor();
    fdef.density = 0.05f;
    if(bodyData.getDensity() > 0) {
      fdef.density = bodyData.getDensity();
    }

    fdef.friction = 1; //= no sliding along the object
    fdef.restitution = 0.9f;
//    if(friendly) {
//      fdef.filter.categoryBits = PLAYER_BITS;
//      fdef.filter.maskBits = MASK_FRIENDLY_BULLET;
//    }
//    else {
//      fdef.filter.categoryBits = NPC_BITS;
//      fdef.filter.maskBits = MASK_ENEMY_BULLET;
//    }
    fdef.filter.categoryBits = BULLET_BITS;
    fdef.filter.maskBits = MASK_BULLET_BITS;

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

  public static Body createGalaxyBody(TiledMultiMapRenderer renderer) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.StaticBody;
    Body body = world.createBody(bodyDef);

    CircleShape circleShape = new CircleShape();
    circleShape.setRadius((renderer.getWorldPixelsX() / 2 ) * Settings.MPP);

    FixtureDef fdef = new FixtureDef();
    fdef.shape = circleShape;
    fdef.isSensor = true;
    fdef.filter.groupIndex = 0;
    fdef.filter.categoryBits = WORLD_BITS;
    fdef.filter.maskBits = MASK_WORLD;
    body.createFixture(fdef);

    float positionX = renderer.getWorldPixelsX() / 2 * Settings.MPP;
    float positionY = renderer.getWorldPixelsY() / 2 * Settings.MPP;
    body.setTransform(new Vector2(positionX, positionY), 0);

    circleShape.dispose();
    return body;
  }
}
