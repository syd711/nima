package com.starsailor.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.starsailor.actors.RoutedNPC;
import com.starsailor.components.RoutingComponent;
import com.starsailor.managers.SteeringManager;

/**
 * Let the give npc follow its route.
 */
public class RouteState implements State<RoutedNPC> {
  @Override
  public void enter(RoutedNPC npc) {
    RoutingComponent routingComponent = npc.getComponent(RoutingComponent.class);
    Array<Vector2> wayPoints = routingComponent.getWayPoints();

    SteeringManager.setRouteSteering(npc, wayPoints);
  }

  @Override
  public void update(RoutedNPC npc) {
  }

  @Override
  public void exit(RoutedNPC npc) {

  }

  @Override
  public boolean onMessage(RoutedNPC npc, Telegram telegram) {
    return false;
  }
}
