package com.starsailor.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Ship;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.data.WeaponProfile;
import com.starsailor.managers.EntityManager;
import com.starsailor.managers.SteeringManager;

import java.util.List;

/**
 *
 */
public class AttackState extends NPCState implements State<NPC> {
  private List<Ship> attackingGroupMembers;
  private Ship enemy;

  public AttackState(Ship enemy) {
    this.enemy = enemy;
    this.attackingGroupMembers = enemy.formationComponent.getMembers();
  }

  /**
   * Called for each additional hit
   * @param bullet the bullet that hit the ship of this state
   */
  public void hitBy(Bullet bullet) {
    if(!bullet.isFriendlyFire()) {
      this.enemy = bullet.owner;
    }
  }

  @Override
  public void enter(NPC npc) {
  }

  @Override
  public void update(NPC npc) {
    //update the list of active enemies, maybe one was destroyed
    attackingGroupMembers = EntityManager.getInstance().filterAliveEntities(attackingGroupMembers);

    //check if there are more attackers first
    if(attackingGroupMembers.isEmpty()) {
      //..and return to default state
      npc.switchToDefaultState();
      return;
    }

    //-------------- There are enemies -----------------------------
    //there are enemies, so find the nearest one
    enemy = findNearestEnemyOfGroup(npc, attackingGroupMembers);

    //change steering, may be we are close enough sicne we are in the arrive steering
    if(isInAttackDistance(npc, enemy)) {
      //increase during attack
      float radius = npc.steerableComponent.getBoundingRadius() + 100;
      SteeringManager.setFaceSteering(npc.steerableComponent, enemy.steerableComponent, radius);
    }
    else {
      SteeringManager.setAttackSteering(npc.steerableComponent, enemy.steerableComponent);
      //if the attacker is not in attacking distance, we skip here
      return;
    }


    //the primary weapon attack
    List<WeaponProfile> primaryChargedWeapons = getChargedWeaponsForCategory(npc, WeaponProfile.Category.PRIMARY);
    fireWeapons(npc, enemy, primaryChargedWeapons);

    //no check if I am a locked target (e.g. missiles firing at me!)
    Bullet enemyBullet = findEnemyBulletTargetedFor(npc);
    if(enemyBullet != null) {
      List<WeaponProfile> chargedDefensiveWeapons = getChargedDefensiveWeaponsFor(npc, enemyBullet);
      fireWeapons(npc, enemy, chargedDefensiveWeapons);
    }


    //check shield state
    if(!npc.shieldComponent.isActive()) {
      //fire seconds weapons if there is no shield anymore
      List<WeaponProfile> secondaryChargedWeapons = getChargedWeaponsForCategory(npc, WeaponProfile.Category.SECONDARY);
      fireWeapons(npc, enemy, secondaryChargedWeapons);
    }
    else {
      //we are finished here and can start the next iteration
      return;
    }

    float healthPercentage = npc.healthComponent.getPercent();
    if(healthPercentage > 50) {
      List<WeaponProfile> emergencyChargedWeapons = getChargedWeaponsForCategory(npc, WeaponProfile.Category.EMERGENCY);
      fireWeapons(npc, enemy, emergencyChargedWeapons);
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
