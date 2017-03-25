package com.starsailor.util.box2d;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.starsailor.actors.Galaxy;
import com.starsailor.actors.Player;
import com.starsailor.util.Settings;

import static com.starsailor.util.Settings.MPP;

/**
 * Box2d utilities
 */
public class Box2dUtil {

  /**
   * Both parameters are in box2d format
   */
  public static float getBox2dAngle(Vector2 from, Vector2 to) {
    return (float) Math.atan2(from.y - to.y, from.x - to.x);
  }

  public static Vector2 toWorldPoint(Vector2 coordinates) {
    return coordinates.scl(Settings.PPM);
  }

  public static Vector2 toBox2Vector(Vector2 vector) {
    return new Vector2(vector.x * MPP, vector.y * MPP);
  }

  public static Entity getEntityAtClickPoint(World world, Vector2 clickPoint) {
    Vector2 box2dPoint = new Vector2(clickPoint.x * Settings.MPP, clickPoint.y * Settings.MPP);
    return getEntityAtBox2dPoint(world, box2dPoint);
  }

  public static boolean isInsideWorld(World world, Vector2 box2dPoint) {
    //Check to see if there's a body under the mouse cursor.
    Array<Body> bodyArray = new Array<>();
    world.getBodies(bodyArray);

    for(Body body : bodyArray) {
      Array<Fixture> fixtureList = body.getFixtureList();
      for(Fixture fixture : fixtureList) {
        if(fixture.testPoint(box2dPoint)) {
          Entity userData = (Entity) body.getUserData();
          if(userData instanceof Galaxy) {
            return true;
          }
        }
      }
    }
    return false;
  }

  public static Entity getEntityAtBox2dPoint(World world, Vector2 box2dPoint) {
    //Check to see if there's a body under the mouse cursor.
    Array<Body> bodyArray = new Array<>();
    world.getBodies(bodyArray);

    for(Body body : bodyArray) {
      Array<Fixture> fixtureList = body.getFixtureList();
      for(Fixture fixture : fixtureList) {
        if(fixture.testPoint(box2dPoint)) {
          Entity userData = (Entity) body.getUserData();
          if(userData instanceof Galaxy) {
            continue;
          }
          if(userData instanceof Player) {
            continue;
          }
          //TODO click on entity during location contact
//          if(userData instanceof Location) {
//            Gdx.app.log(Box2dUtil.class.getName(), "Igonoring location for left click");
//            continue;
//          }
          return userData;
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
}
