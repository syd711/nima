package com.starsailor.actors.states.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Ship;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.managers.EntityManager;
import com.starsailor.managers.SteeringManager;

import java.util.List;

/**
 * Let the give npc follow its route.
 */
public class FleeFromAttackerAndWaitState extends NPCState implements State<NPC> {
  private Bullet bullet;
  private List<Ship> formationMembers;

  /**
   * Bullet will always be an enemy bullet
   * @param bullet
   */
  public FleeFromAttackerAndWaitState(Bullet bullet) {
    formationMembers = bullet.owner.formationComponent.getMembers();
    this.bullet = bullet;
  }

  @Override
  public void enter(NPC npc) {
    Gdx.app.log(getClass().getName(), npc + " entered FleeFromAttackerState");
    SteeringManager.setFleeSteering(npc.steerableComponent, bullet.owner.steerableComponent);
  }

  @Override
  public void update(NPC npc) {
    //check if all attacker members are destroyed, return to default state then
    List<Ship> filteredMembers = EntityManager.getInstance().filterAliveEntities(formationMembers);
    if(filteredMembers.isEmpty()) {
      npc.switchToDefaultState();
      return;
    }

    //check if all enemies are out of range, btw. all members are in default state
    if(iAmTheOnlyOneNotInDefaultState(npc)) {
      npc.switchToDefaultState();
      return;
    }

    //check max distance to all enemies
    for(Ship filteredMember : filteredMembers) {
      float distanceTo = npc.getDistanceTo(filteredMember);
      //simply use the duplicate attack distance
      float shootingDistanceWithOffset = bullet.owner.shipProfile.attackDistance * 2;
      if(distanceTo > shootingDistanceWithOffset) {
        npc.steerableComponent.setBehavior(null);
      }
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
