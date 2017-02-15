package com.starsailor.actors.states.npc;

import com.starsailor.actors.NPC;
import com.starsailor.actors.Ship;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.actors.bullets.BulletFactory;
import com.starsailor.data.WeaponProfile;
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
   *
   * @param ship
   * @param enemy
   */
  protected boolean isInAttackDistance(Ship ship, Ship enemy) {
    float attackDistance = ship.shipProfile.attackDistance;
    float distanceToEnemy = ship.getDistanceTo(enemy);
    return distanceToEnemy < attackDistance;
  }

  /**
   * Returns the bullet that is currently on it's way to me :(
   * We ignore the fact if it is a friendly or enemy bullet since we never fire at friends but can hit them.
   *
   * @param npc the npc to check for bullets for
   */
  @Nullable
  protected Bullet findEnemyBulletTargetedFor(NPC npc) {
    List<Bullet> bullets = EntityManager.getInstance().getEntities(Bullet.class);
    for(Bullet bullet : bullets) {
      //one is enough
      if(bullet.target.equals(npc)) {
        return bullet;
      }
    }
    return null;
  }

  /**
   * Checks if the bullet that is currently targeted for the given npc can be defended by another weapon.
   *
   * @param npc         the npc that wants to defend itself
   * @param enemyBullet the enemy bullet that should be defended
   */
  protected List<WeaponProfile> getChargedDefensiveWeaponsFor(NPC npc, Bullet enemyBullet) {
    List<WeaponProfile> result = new ArrayList<>();
    List<WeaponProfile.Types> defensiveWeapons = enemyBullet.getDefensiveWeapons();
    for(WeaponProfile.Types defensiveWeapon : defensiveWeapons) {
      //find matching weapon for the given ship
      List<WeaponProfile> weaponProfiles = npc.shipProfile.weaponProfiles;
      for(WeaponProfile weaponProfile : weaponProfiles) {
        if(weaponProfile.type.equals(defensiveWeapon)) {
          boolean charged = npc.shootingComponent.isCharged(weaponProfile);
          if(charged) {
            result.add(weaponProfile);
          }
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
    for(WeaponProfile weapon : ship.getWeapons()) {
      if(!weapon.getCategory().equals(weaponCategory)) {
        continue;
      }

      boolean charged = ship.shootingComponent.isCharged(weapon);
      if(charged) {
        result.add(weapon);
      }
    }
    return result;
  }

  /**
   * Fires a bullet using the active weapon profile
   */
  private void fireAtTarget(Ship attacker, Ship attacking, WeaponProfile weaponProfile) {
    BulletFactory.create(attacker, attacking, weaponProfile);
  }

  /**
   * Fires all weapons of the given weapon profile list
   * @param attacker
   * @param attacking
   * @param weaponProfiles
   */
  protected void fireWeapons(Ship attacker, Ship attacking, List<WeaponProfile> weaponProfiles) {
    for(WeaponProfile chargedWeapon : weaponProfiles) {
      fireAtTarget(attacker, attacking, chargedWeapon);
    }
  }
}
