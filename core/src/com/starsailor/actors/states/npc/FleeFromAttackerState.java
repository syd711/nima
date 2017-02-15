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
public class FleeFromAttackerState extends NPCState implements State<NPC> {
  private Bullet bullet;
  private List<Ship> formationMembers;

  /**
   * Bullet will always be an enemy bullet
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
    List<Ship> filteredMembers = EntityManager.getInstance().filterAliveEntities(formationMembers);
    if(filteredMembers.isEmpty()) {
      npc.switchToDefaultState();
      return;
    }

    //check max distance to all enemies
    for(Ship filteredMember : filteredMembers) {
      float distanceTo = npc.getDistanceTo(filteredMember);
      float shootingDistanceWithOffset = bullet.owner.shipProfile.shootDistance * 3;
      if(distanceTo > shootingDistanceWithOffset) {
        npc.steerableComponent.setBehavior(null);
        npc.getStateMachine().changeState(NPCStates.IDLE);
      }
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
