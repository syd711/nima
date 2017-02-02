package com.starsailor.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.behaviors.CollisionAvoidance;
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Player;
import com.starsailor.components.SteerableComponent;

/**
 *
 */
public class AvoidCollisionState implements State<NPC> {
  @Override
  public void enter(NPC npc) {
    SteerableComponent steerableComponent = npc.getComponent(SteerableComponent.class);

    float radius = 10f;
    Array<Steerable<Vector2>> obstacles = new Array<>();
    obstacles.add(Player.getInstance().steerableComponent);
    RadiusProximity<Vector2> radiusProximity = new RadiusProximity<>(steerableComponent, obstacles, radius);
    CollisionAvoidance<Vector2> collisionAvoidance = new CollisionAvoidance(steerableComponent, radiusProximity);
    steerableComponent.setBehavior(collisionAvoidance);
  }

  @Override
  public void update(NPC npc) {
//    float distanceToPlayer = npc.getDistanceToPlayer();
//    if(distanceToPlayer > npc.shipProfile.evadeDistance) {
//      npc.getStateMachine().changeState(new RouteState());
//    }
  }

  @Override
  public void exit(NPC npc) {

  }

  @Override
  public boolean onMessage(NPC npc, Telegram telegram) {
    return false;
  }
}
