package com.nima.util;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.nima.actors.Spine;

import static com.nima.util.Settings.MPP;

/**
 * Box2d utilities
 */
public class Box2dUtil {

  /**
   * Both parameters are in box2d format
   * @param from
   * @param to
   * @return
   */
  public static float getBox2dAngle(Vector2 from, Vector2 to) {
    return (float) Math.atan2(from.y - to.y, from.x - to.x);
  }

  public static Vector2 toBox2Vector(Vector2 vector) {
    return new Vector2(vector.x*MPP, vector.y*MPP);
  }

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
    fdef.isSensor = true;
    fdef.density = 1;
    fdef.restitution = 0.9f;
    fdef.shape = shape;
    b.createFixture(fdef);
    shape.dispose();

    return b;
  }


  public static Entity getEntityAt(World world, Vector2 clickPoint) {
    Vector2 box2dPoint = new Vector2(clickPoint.x * Settings.MPP, clickPoint.y * Settings.MPP);

    //Check to see if there's a body under the mouse cursor.
    Array<Body> bodyArray = new Array<>();
    world.getBodies(bodyArray);

    for(Body body : bodyArray) {
      Array<Fixture> fixtureList = body.getFixtureList();
      for(Fixture fixture : fixtureList) {
        if(fixture.testPoint(box2dPoint)) {
          return (Entity) body.getUserData();
        }
      }
    }
    return null;
  }





//  @Override
//  public void update() {
//    if(routingComponent != null) {
//
//      if(scalingComponent.isChanging()) {
//        return;
//      }
//      //TODO build state machine and move to routing component
//      float currentAngle = GraphicsUtil.getAngle(positionComponent.getPosition(), routingComponent.target);
//      Vector2 delta = GraphicsUtil.getDelta(currentAngle, 2f);
//      Vector2 box2dDelta = Box2dUtil.toBox2Vector(delta);
//      float x = box2dDelta.x;
//      float y = box2dDelta.y;
//      Vector2 position = bodyComponent.body.getPosition();
//
//      if(currentAngle >= 0 && currentAngle <= 90) {
//        position.x = position.x + x;
//        position.y = position.y + y;
//      }
//      else if(currentAngle > 90 && currentAngle <= 180) {
//        position.x = position.x - x;
//        position.y = position.y + y;
//      }
//      else if(currentAngle < 0 && currentAngle >= -90) {
//        position.x = position.x + x;
//        position.y = position.y - y;
//      }
//      else if(currentAngle < -90 && currentAngle >= -180) {
//        position.x = position.x - x;
//        position.y = position.y - y;
//      }
//
//      bodyComponent.body.setTransform(position, bodyComponent.body.getAngle());
//    }
//  }
}
