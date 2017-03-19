package com.starsailor;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.starsailor.actors.Collidable;
import com.starsailor.actors.GameEntity;
import com.starsailor.util.box2d.Box2dUtil;

/**
 * The Box2d world contact listener
 */
public class GameContactListener implements ContactListener {
  @Override
  public void beginContact(Contact contact) {
    Entity userDataA = (Entity) contact.getFixtureA().getBody().getUserData();
    Entity userDataB = (Entity) contact.getFixtureB().getBody().getUserData();

    Vector2 position = Box2dUtil.toWorldPoint(contact.getWorldManifold().getPoints()[0]);
//    System.out.println(userDataA + " ################## " + userDataB + " at " + position);
    Collidable component = userDataA.getComponent(Collidable.class);
    if(component == null) {
      component = userDataB.getComponent(Collidable.class);
    }

    if(component != null) {
      component.handleCollision(userDataB, position);
    }
  }

  @Override
  public void endContact(Contact contact) {
    GameEntity userDataA = (GameEntity) contact.getFixtureA().getBody().getUserData();
    GameEntity userDataB = (GameEntity) contact.getFixtureB().getBody().getUserData();

//    System.out.println("End Contact of " + userDataA + "/" + userDataB);
  }

  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {

  }

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) {

  }
}
