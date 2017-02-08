package com.starsailor.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.starsailor.actors.NPC;
import com.starsailor.components.RoutingComponent;
import com.starsailor.managers.SteeringManager;

/**
 *
 */
public class RoutedSeekAndDestroyState extends NPCState implements State<NPC> {
  @Override
  public void enter(NPC npc) {
    RoutingComponent routingComponent = npc.getComponent(RoutingComponent.class);
    Array<Vector2> wayPoints = routingComponent.getWayPoints();
    SteeringManager.setRouteSteering(npc, wayPoints);
  }

  @Override
  public void update(NPC npc) {
    if(findAndLockNearestTarget(npc)) {
      SteeringManager.setBattleSteering(npc);
      npc.getStateMachine().changeState(new AttackState());
    }
  }

  @Override
  public void exit(NPC npc) {

  }

  @Override
  public boolean onMessage(NPC npc, Telegram telegram) {
    return false;
  }
}
