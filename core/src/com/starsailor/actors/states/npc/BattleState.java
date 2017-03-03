package com.starsailor.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Ship;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.managers.EntityManager;

import java.util.List;

/**
 *
 */
abstract public class BattleState extends NPCState implements State<NPC> {
  private List<Ship> attackingGroupMembers;

  public void updateEnemy(Ship enemy) {
    attackingGroupMembers = enemy.formationComponent.getMembers();
  }

  protected List<Ship> getEnemies() {
    attackingGroupMembers = EntityManager.getInstance().filterAliveEntities(attackingGroupMembers);
    return attackingGroupMembers;
  }

  /**
   * Updates the list of enemies depending from whom the bullet was fired
   * @param npc the attacked npc
   * @param bullet
   * @return true if the bullet was meant for this npc
   */
  protected boolean updateStateForBullet(NPC npc, Bullet bullet) {
    if(bullet.attackedMemberOf(npc)) {
      updateEnemyList(bullet, attackingGroupMembers);
      return true;
    }
    return false;
  }
}
