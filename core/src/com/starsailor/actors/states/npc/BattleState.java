package com.starsailor.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Ship;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.managers.EntityManager;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
abstract public class BattleState extends NPCState implements State<NPC> {
  private List<Ship> attackingGroupMembers = new ArrayList<>();

  public void updateEnemyList(Ship enemy) {
    List<Ship> members = enemy.formationComponent.getMembers();
    for(Ship member : members) {
      if(!attackingGroupMembers.contains(member)) {
        attackingGroupMembers.add(member);
      }
    }
  }

  /**
   * Returns the list of active enemies
   * @return
   */
  protected List<Ship> getEnemies() {
    EntityManager.getInstance().filterAliveEntities(attackingGroupMembers);
    return attackingGroupMembers;
  }

  /**
   * Updates the list of enemies depending from whom the bullet was fired
   * @param npc the attacked npc
   * @param bullet
   * @return true if the bullet was meant for this npc
   */
  protected boolean updateStateForBullet(NPC npc, Bullet bullet) {
    //does this bullet concern me?
    if(bullet.attackedMemberOf(npc)) {
      updateEnemyList(bullet.owner);
      return true;
    }
    return false;
  }
}
