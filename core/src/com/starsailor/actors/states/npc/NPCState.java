package com.starsailor.actors.states.npc;

import com.starsailor.actors.NPC;
import com.starsailor.actors.Player;
import com.starsailor.actors.Ship;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.data.WeaponProfile;
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
   * Used for search and destroy.
   * Instead of search for the next enemy of an enemy group, we
   * use all instances of ships to find the nearest target for an attack
   */
  @Nullable
  protected Ship findNearestEnemy(NPC npc) {
    List<Ship> entities = EntityManager.getInstance().getEntities(Ship.class);
    return findNearestEnemyOfGroup(npc, entities);
  }

  /**
   * Returns another ship that is inside the closest attack range and an enemy.
   * The entities "attackDistance" is used for this which means
   * that the ship itself has not necessarily a weapon in shooting range.
   *
   * @param npc the npc to search an enemy for
   */
  @Nullable
  protected Ship findNearestEnemyOfGroup(NPC npc, List<? extends Ship> group) {
    Ship enemy = null;
    for(Ship ship : group) {
      if(!ship.isEnemyOf(npc)) {
        continue;
      }

      float distanceToEnemy = ship.getDistanceTo(npc);
      if(distanceToEnemy != 0) {
        if(enemy == null) {
          enemy = ship;
        }
        //may another ship is closer?
        else if(distanceToEnemy < ship.getDistanceTo(enemy)) {
          enemy = ship;
        }
      }
    }
    return enemy;
  }

  /**
   * Returns true if the enemy is in attack range of the ship
   */
  protected boolean isInAttackingDistance(Ship ship, Ship enemy) {
    float attackDistance = ship.shipProfile.attackDistance;
    float distanceToEnemy = ship.getDistanceTo(enemy);
    return distanceToEnemy < attackDistance;
  }

  /**
   * Returns true if the enemy is in shooting range of the ship
   */
  protected boolean isInShootingDistance(Ship ship, Ship enemy) {
    float attackDistance = ship.shipProfile.shootDistance;
    float distanceToEnemy = ship.getDistanceTo(enemy);
    return distanceToEnemy < attackDistance;
  }

  /**
   * Returns true if the enemy is in retreating range of the ship
   */
  protected boolean isInRetreatingDistance(Ship ship, Ship enemy) {
    float retreatDistance = ship.shipProfile.retreatDistance;
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
  protected List<WeaponProfile> getChargedDefensiveWeaponsFor(Ship ship, Bullet enemyBullet) {
    List<WeaponProfile> result = new ArrayList<>();
    List<WeaponProfile.Types> defensiveWeapons = enemyBullet.getDefensiveWeapons();
    for(WeaponProfile.Types defensiveWeapon : defensiveWeapons) {
      //find matching weapon for the given ship
      for(WeaponProfile weaponProfile : ship.getChargedWeapons()) {
        if(weaponProfile.type.equals(defensiveWeapon)) {
          result.add(weaponProfile);
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
  protected List<WeaponProfile> getChargedWeaponsForCategory(Ship ship, WeaponProfile.Category weaponCategory) {
    List<WeaponProfile> result = new ArrayList<>();
    for(WeaponProfile weapon : ship.getChargedWeapons()) {
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
  protected void fireWeapons(Ship attacker, Ship attacking, List<WeaponProfile> weaponProfiles) {
    for(WeaponProfile chargedWeapon : weaponProfiles) {
      BulletManager.getInstance().create(attacker, attacking, chargedWeapon);
    }
  }
}
