package com.nima;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.nima.actors.Location;
import com.nima.actors.NPC;
import com.nima.actors.Player;
import com.nima.actors.states.NPCState;
import com.nima.actors.states.PlayerState;
import com.nima.data.RoutePoint;

/**
 * The Box2d world contact listener
 */
public class GameContactListener implements ContactListener {
  @Override
  public void beginContact(Contact contact) {
    Object userDataA = contact.getFixtureA().getBody().getUserData();
    Object userDataB = contact.getFixtureB().getBody().getUserData();

    System.out.println(userDataA + " contacted " + userDataB);

    if(userDataA instanceof Player && userDataB instanceof Location) {
      if(Player.getInstance().target != null && Player.getInstance().target.equals(userDataB)) {
        Player.getInstance().getStateMachine().changeState(PlayerState.DOCK_TO_STATION);
      }
    }

    if(userDataA instanceof NPC && userDataB instanceof RoutePoint) {
      NPC npc = (NPC) userDataA;
      npc.getStateMachine().changeState(NPCState.ROUTE_POINT_ARRIVED);
    }

//    EntityManager.getInstance().notifyCollisionStart(userDataA, userDataB);
  }

  @Override
  public void endContact(Contact contact) {
    Object userDataA = contact.getFixtureA().getBody().getUserData();
    Object userDataB = contact.getFixtureB().getBody().getUserData();

//    EntityManager.getInstance().notifyCollisionEnd(userDataA, userDataB);
  }

  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {

  }

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) {

  }
}
