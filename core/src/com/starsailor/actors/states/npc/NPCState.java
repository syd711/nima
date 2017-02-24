package com.starsailor.actors.states.npc;

import com.starsailor.actors.Ship;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.data.WeaponData;
import com.starsailor.managers.BulletManager;
import com.starsailor.managers.EntityManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract superstate for all npc with different helper methods
 */
abstract public class NPCState {

  /**
   * Returns true if the enemy is in attack range of the ship
   */
  protected boolean isInAttackingDistance(Ship ship, Ship enemy) {
    float attackDistance = ship.shipData.getAttackDistance();
    float distanceToEnemy = ship.getDistanceTo(enemy);
    return distanceToEnemy < attackDistance;
  }

  /**
   * Returns true if the enemy is in shooting range of the ship
   */
  protected boolean isInShootingDistance(Ship ship, Ship enemy) {
    float attackDistance = ship.shipData.getShootDistance();
    float distanceToEnemy = ship.getDistanceTo(enemy);
    return distanceToEnemy < attackDistance;
  }

  /**
   * Returns true if the enemy is in retreating range of the ship
   */
  protected boolean isInRetreatingDistance(Ship ship, Ship enemy) {
    float retreatDistance = ship.shipData.getRetreatDistance();
    float distanceToEnemy = ship.getDistanceTo(enemy);
    return distanceToEnemy > retreatDistance;
  }

  /**
   * Returns true of the whole group is in retreating distance,
   * so the enemy is out of range and the group returns to the default state.
   */
  protected boolean isGroupInRetreatingDistance(Ship ship, Ship enemy) {
    List<Ship> members = ship.formationComponent.getMembers();
    for(Ship member : members) {
      if(!isInRetreatingDistance(member, enemy)) {
        return false;
      }
    }
    return true;
  }

  protected boolean iAmTheOnlyOneNotInDefaultState(Ship ship) {
    List<Ship> members = ship.formationComponent.getMembers();
    for(Ship member : members) {
      if(member.equals(ship)) {
        continue;
      }

      if(!member.isInDefaultState()) {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns the bullet that is currently on it's way to me :(
   * We ignore the fact if it is a friendly or enemy bullet since we never fire at friends but can hit them.
   *
   * @param ship the ship to check for bullets for
   */
  @Nullable
  protected Bullet findEnemyBulletTargetedFor(Ship ship) {
    List<Bullet> bullets = EntityManager.getInstance().getEntities(Bullet.class);
    for(Bullet bullet : bullets) {
      //ignore the bullets that currently hit the ship
      if(bullet.isMarkedForDestroy()) {
        continue;
      }

      //one is enough
      if(bullet.target.equals(ship)) {
        return bullet;
      }
    }
    return null;
  }

  /**
   * Checks if the bullet that is currently targeted for the given ship can be defended by another weapon.
   *
   * @param ship        the ship that wants to defend itself
   * @param enemyBullet the enemy bullet that should be defended
   */
  protected List<WeaponData> getChargedDefensiveWeaponsFor(Ship ship, Bullet enemyBullet) {
    List<WeaponData> result = new ArrayList<>();
    List<WeaponData.Types> defensiveWeapons = enemyBullet.getDefensiveWeapons();
    for(WeaponData.Types defensiveWeapon : defensiveWeapons) {
      //find matching weapon for the given ship
      for(WeaponData weaponData : ship.getChargedWeapons()) {
        if(weaponData.type.equals(defensiveWeapon)) {
          result.add(weaponData);
        }
      }
    }
    return result;
  }

  /**
   * Returns the weapons that are charged and match the given category.
   *
   * @param ship           the ship to retrieve the information for
   * @param weaponCategory the category
   */
  protected List<WeaponData> getChargedWeaponsForCategory(Ship ship, WeaponData.Category weaponCategory) {
    List<WeaponData> result = new ArrayList<>();
    for(WeaponData weapon : ship.getChargedWeapons()) {
      if(!weapon.getCategory().equals(weaponCategory)) {
        continue;
      }

      result.add(weapon);
    }
    return result;
  }

  /**
   * Fires all weapons of the given weapon profile list
   */
  protected void fireWeapons(Ship attacker, Ship attacking, List<WeaponData> weaponDatas) {
    for(WeaponData chargedWeapon : weaponDatas) {
      BulletManager.getInstance().create(attacker, attacking, chargedWeapon);
    }
  }

  /**
   * Updates the list of current enemy for the bullet that has hit the ship.
   *
   * @param bullet       the bullet that has hit the ship
   * @param enemyTargets the current list of enemies
   */
  protected void updateEnemyList(Bullet bullet, List<Ship> enemyTargets) {
    List<Ship> members = bullet.owner.formationComponent.getMembers();
    for(Ship member : members) {
      if(!enemyTargets.contains(member)) {
        enemyTargets.add(member);
      }
    }
  }
}
