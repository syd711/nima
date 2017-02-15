package com.starsailor.actors.states.npc;

import com.badlogic.gdx.Gdx;
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
    Gdx.app.log(getClass().getName(), npc + " entered AttackState");
    SteeringManager.setArriveAndFaceSteering(npc, enemy.steerableComponent);
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
    Ship nearestEnemyOfGroup = findNearestEnemyOfGroup(npc, attackingGroupMembers);
    //check range, maybe adept steering
    if(!isInAttackDistance(npc, nearestEnemyOfGroup)) {
      //TODO
    }

    if(!nearestEnemyOfGroup.equals(enemy)) {
      enemy = nearestEnemyOfGroup;
      //update steering if we have a new target
      SteeringManager.setArriveAndFaceSteering(npc, nearestEnemyOfGroup.steerableComponent);
    }

    //the primary weapon attack
    List<WeaponProfile> primaryChargedWeapons = getChargedWeaponsForCategory(npc, WeaponProfile.Category.PRIMARY);
    for(WeaponProfile chargedWeapon : primaryChargedWeapons) {
      fireAtTarget(npc, nearestEnemyOfGroup, chargedWeapon);
    }

    //no check if I am a locked target (e.g. missiles firing at me!)
    Bullet enemyBullet = findEnemyBulletTargetedFor(npc);
    fireDefensiveWeaponsFor(npc, enemyBullet);

    //check shield state
    if(!npc.shieldComponent.isActive()) {
      //fire seconds weapons if there is no shield anymore
      List<WeaponProfile> secondaryChargedWeapons = getChargedWeaponsForCategory(npc, WeaponProfile.Category.SECONDARY);
      for(WeaponProfile chargedWeapon : secondaryChargedWeapons) {
        fireAtTarget(npc, nearestEnemyOfGroup, chargedWeapon);
      }
    }
    else {
      //we are finished here and can start the next iteration
      return;
    }

    float healthPercentage = npc.healthComponent.getPercent();
    if(healthPercentage > 50) {
      List<WeaponProfile> emergencyChargedWeapons = getChargedWeaponsForCategory(npc, WeaponProfile.Category.EMERGENCY);
      for(WeaponProfile chargedWeapon : emergencyChargedWeapons) {
        fireAtTarget(npc, nearestEnemyOfGroup, chargedWeapon);
      }
    }
    else if(healthPercentage < 30) {
      List<WeaponProfile> defensiveChargedWeapons = getChargedWeaponsForCategory(npc, WeaponProfile.Category.DEFENSIVE);
      for(WeaponProfile chargedWeapon : defensiveChargedWeapons) {
        fireAtTarget(npc, nearestEnemyOfGroup, chargedWeapon);
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
