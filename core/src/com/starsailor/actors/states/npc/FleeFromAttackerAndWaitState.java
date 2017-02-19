package com.starsailor.actors.states.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Ship;
import com.starsailor.managers.SteeringManager;

import java.util.List;

/**
 * Let the give npc follow its route.
 */
public class FleeFromAttackerAndWaitState extends NPCState implements State<NPC> {
  private List<Ship> attackingGroupMembers;

  public FleeFromAttackerAndWaitState(Ship enemy) {
    attackingGroupMembers = enemy.formationComponent.getMembers();
  }

  @Override
  public void enter(NPC npc) {
    Gdx.app.log(getClass().getName(), npc + " entered FleeFromAttackerState");
    npc.setStateVisible(true);

    Ship nearestEnemy = findNearestEnemyOfGroup(npc, attackingGroupMembers);
    SteeringManager.setFleeSteering(npc.steerableComponent, nearestEnemy.steerableComponent);
  }

  @Override
  public void update(NPC npc) {
    //check if all enemies are out of range, btw. all members are in default state
    if(iAmTheOnlyOneNotInDefaultState(npc)) {
      npc.switchToDefaultState();
      return;
    }

    //check max distance to all enemies
    Ship nearestEnemy = findNearestEnemyOfGroup(npc, attackingGroupMembers);
    float distanceTo = npc.getDistanceTo(nearestEnemy);
    //simply use the duplicate attack distance
    float shootingDistanceWithOffset = nearestEnemy.shipProfile.attackDistance * 2;
    if(distanceTo > shootingDistanceWithOffset) {
      npc.steerableComponent.setBehavior(null);
    }
    else if(npc.steerableComponent.getBehavior() == null) {
      SteeringManager.setFleeSteering(npc.steerableComponent, nearestEnemy.steerableComponent);
    }
  }

  @Override
  public void exit(NPC npc) {
    npc.setStateVisible(false);
  }

  @Override
  public boolean onMessage(NPC npc, Telegram telegram) {
    return false;
  }
}
