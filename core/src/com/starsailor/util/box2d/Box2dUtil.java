package com.starsailor.util.box2d;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.starsailor.util.Settings;

import static com.starsailor.util.Settings.MPP;

/**
 * Box2d utilities
 */
public class Box2dUtil {

  /**
   * Both parameters are in box2d format
   *
   * @param from
   * @param to
   * @return
   */
  public static float getBox2dAngle(Vector2 from, Vector2 to) {
    return (float) Math.atan2(from.y - to.y, from.x - to.x);
  }

  public static Vector2 toWorldPoint(Vector2 coordinates) {
    return coordinates.scl(Settings.PPM);
  }

  public static Body clickBody(Vector2 clickPoint) {
    return BodyGenerator.createClickBody(clickPoint);
  }

  public static Vector2 toBox2Vector(Vector2 vector) {
    return new Vector2(vector.x * MPP, vector.y * MPP);
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

  /**
   * Point gravity: uses the given target as gravity point to let
   * the given source body move to it
   * @param source the body the gravity is applied to
   * @param target the body that has gravity
   * @param gravity the gravity value
   */
  public static void gravity(Body source, Body target, float gravity) {
    float G = gravity; //modifier of gravity value - you can make it bigger to have stronger gravity
    Vector2 targetPosition = target.getPosition();
    Vector2 myPosition = source.getPosition();
    float distance = myPosition.dst(targetPosition);
    float forceValue = G / (distance * distance);
    Vector2 direction = new Vector2(targetPosition).sub(new Vector2(myPosition));
    source.applyForceToCenter(direction.scl(forceValue), true);
  }

  public static float addDegree(float angle, int degree) {
    return (float) (angle + Math.toRadians(degree));
  }

  public static void updateAngle(Body body, Vector2 target) {
    body.setAngularVelocity(0);
    float bodyAngle = body.getAngle();
    float DEGTORAD = (float) Math.toRadians(4f);

    Vector2 source = body.getPosition();
    Vector2 toTarget = new Vector2(target).sub(source);
    //       0
    //   +++++++++
    //   + 1 + 2 +
    //90 +++++++++ -90
    //   + 4 + 3 +
    //   +++++++++
    //     -180
    float desiredAngle = (float) Math.atan2(-toTarget.x, -toTarget.y); //1
    if(source.x < target.x && source.y < target.y) {
      desiredAngle = (float) Math.atan2(toTarget.x, toTarget.y); // 2
    }
    else if(source.x < target.x && source.y > target.y) {
      desiredAngle = (float) Math.atan2(-toTarget.x, toTarget.y); //3
    }
    else if(source.x > target.x && source.y > target.y) {
      desiredAngle = (float) Math.atan2(toTarget.x, -toTarget.y); //4
    }

    float nextAngle = bodyAngle + body.getAngularVelocity() / 60.0f;
    float desiredAngleDegrees = (float) Math.toDegrees(desiredAngle);
    System.out.println(desiredAngleDegrees);
    float totalRotation = desiredAngle - nextAngle;
    while(totalRotation < -180 * DEGTORAD) totalRotation += 360 * DEGTORAD;
    while(totalRotation > 180 * DEGTORAD) totalRotation -= 360 * DEGTORAD;

    float change = (1 * DEGTORAD); //allow 1 degree rotation per time step
    float newAngle = bodyAngle + Math.min(change, Math.max(-change, totalRotation));
    body.setTransform(source, newAngle);
  }

//  @Override
//  public void updateAngle() {
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
