package com.starsailor.actors.states.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Ship;
import com.starsailor.managers.SteeringManager;

/**
 *
 */
public class RoutedSeekAndDestroyState extends NPCState implements State<NPC> {
  @Override
  public void enter(NPC npc) {
    Gdx.app.log(getClass().getName(), npc + " entered RoutedSeekAndDestroyState");
    SteeringManager.setRouteSteering(npc.steerableComponent, npc.routingComponent, npc.getCenter());
  }

  @Override
  public void update(NPC npc) {
    Ship nearestEnemy = npc.findNearestEnemy(false);
    if(nearestEnemy != null && isInAttackingDistance(npc, nearestEnemy)) {
      npc.switchToBattleState(nearestEnemy);
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
