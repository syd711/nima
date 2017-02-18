package com.starsailor.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Ship;
import com.starsailor.managers.SteeringManager;

/**
 * Let the give npc follow its route.
 */
public class GuardState extends NPCState implements State<NPC> {

  @Override
  public void enter(NPC npc) {
    SteeringManager.setGuardSteering(npc);
  }

  @Override
  public void update(NPC npc) {
    Ship nearestEnemy = findNearestEnemy(npc);
    if(nearestEnemy != null && isInAttackingDistance(npc, nearestEnemy)) {
      npc.switchToBattleState();
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
