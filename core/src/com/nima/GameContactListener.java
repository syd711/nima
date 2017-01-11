package com.nima;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.nima.actors.Location;
import com.nima.actors.Player;
import com.nima.actors.states.PlayerState;

/**
 * The Box2d world contact listener
 */
public class GameContactListener implements ContactListener {
  @Override
  public void beginContact(Contact contact) {
    Entity userDataA = (Entity) contact.getFixtureA().getBody().getUserData();
    Entity userDataB = (Entity) contact.getFixtureB().getBody().getUserData();

    if(userDataA instanceof Player && userDataB instanceof Location) {
      if(Player.getInstance().target != null && Player.getInstance().target.equals(userDataB)) {
        Player.getInstance().getStateMachine().changeState(PlayerState.DOCK_TO_STATION);
      }
    }

//    EntityManager.getInstance().notifyCollisionStart(userDataA, userDataB);
  }

  @Override
  public void endContact(Contact contact) {
    Entity userDataA = (Entity) contact.getFixtureA().getBody().getUserData();
    Entity userDataB = (Entity) contact.getFixtureB().getBody().getUserData();

//    EntityManager.getInstance().notifyCollisionEnd(userDataA, userDataB);
  }

  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {

  }

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) {

  }
}
