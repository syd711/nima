package com.nima.util;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * Box2d utilities
 */
public class Box2dUtil {

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
}
