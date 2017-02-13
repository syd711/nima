package com.starsailor.actors.states.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.NPC;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.managers.EntityManager;
import com.starsailor.managers.SteeringManager;

import java.util.List;

/**
 * Let the give npc follow its route.
 */
public class FleeFromAttackerState extends NPCState implements State<NPC> {
  private Bullet bullet;
  private List<NPC> formationMembers;

  /**
   * Bullet will alway be an enemy bullet
   * @param bullet
   */
  public FleeFromAttackerState(Bullet bullet) {
    formationMembers = bullet.owner.formationComponent.getMembers();
    this.bullet = bullet;
  }

  @Override
  public void enter(NPC npc) {
    Gdx.app.log(getClass().getName(), npc + " entered FleeFromAttackerState");
    SteeringManager.setFleeSteering(npc, bullet.owner);
  }

  @Override
  public void update(NPC npc) {
    //check if all attacker members are destroyed, return to default state then
    List<NPC> filteredMembers = EntityManager.getInstance().filterAliveEntities(formationMembers);
    if(filteredMembers.isEmpty()) {
      npc.getStateMachine().changeState(npc.getDefaultState());
      return;
    }

    //check max distance to all enemies
    for(NPC filteredMember : filteredMembers) {
      float distanceTo = npc.getDistanceTo(filteredMember);
      float shootingDistanceWithOffset = npc.shipProfile.shootDistance + 100;
      if(distanceTo < shootingDistanceWithOffset) {
        SteeringManager.setFleeSteering(npc, filteredMember);
        break;
      }

      npc.steerableComponent.setBehavior(null);
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
