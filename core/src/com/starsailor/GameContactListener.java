package com.starsailor;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.starsailor.actors.Collidable;

/**
 * The Box2d world contact listener
 */
public class GameContactListener implements ContactListener {
  @Override
  public void beginContact(Contact contact) {
    Entity userDataA = (Entity) contact.getFixtureA().getBody().getUserData();
    Entity userDataB = (Entity) contact.getFixtureB().getBody().getUserData();

    System.out.println(userDataA + " ################## " + userDataB);

    Collidable component = userDataA.getComponent(Collidable.class);
    component.handleCollision(userDataA, userDataB);
  }

  @Override
  public void endContact(Contact contact) {
    Entity userDataA = (Entity) contact.getFixtureA().getBody().getUserData();
    Entity userDataB = (Entity) contact.getFixtureB().getBody().getUserData();
  }

  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {

  }

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) {

  }
}
