package com.starsailor.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Ship;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.data.WeaponProfile;
import com.starsailor.managers.EntityManager;
import com.starsailor.managers.SteeringManager;

import java.util.Collections;
import java.util.List;

/**
 *
 */
public class AttackState extends NPCState implements State<NPC> {
  private List<Ship> attackingGroupMembers;

  public AttackState(Ship enemy) {
    this.attackingGroupMembers = enemy.formationComponent.getMembers();
  }

  public AttackState(Bullet bullet) {
    if(bullet.wasFriendlyFire()) {
      this.attackingGroupMembers = Collections.emptyList();
    }
    else {
      this.attackingGroupMembers = bullet.owner.formationComponent.getMembers();
    }
  }

  /**
   * Called for each additional hit
   *
   * @param bullet the bullet that hit the ship of this state
   */
  public void hitBy(Bullet bullet) {
    if(!bullet.wasFriendlyFire()) {
      List<Ship> members = bullet.owner.formationComponent.getMembers();
      for(Ship member : members) {
        if(!this.attackingGroupMembers.contains(member)) {
          this.attackingGroupMembers.add(member);
        }
      }
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
    Ship enemy = findNearestEnemyOfGroup(npc, attackingGroupMembers);

    //update steering
    updateAttackSteering(npc, enemy);

    //and shoot weapons if in attack range
    if(isInAttackDistance(npc, enemy)) {
      fireWeapons(npc, enemy);
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


  //------------- Helper---------------------------------

  /**
   * Fires available weapons depending on the state of the ship
   * @param ship the ship that should fire the weapons and owns this state
   * @param enemy the enemy to shoot at
   */
  private void fireWeapons(Ship ship, Ship enemy) {
    //the primary weapon attack
    List<WeaponProfile> primaryChargedWeapons = getChargedWeaponsForCategory(ship, WeaponProfile.Category.PRIMARY);
    fireWeapons(ship, enemy, primaryChargedWeapons);

    //no check if I am a locked target (e.g. missiles firing at me!)
    Bullet enemyBullet = findEnemyBulletTargetedFor(ship);
    if(enemyBullet != null) {
      List<WeaponProfile> chargedDefensiveWeapons = getChargedDefensiveWeaponsFor(ship, enemyBullet);
      fireWeapons(ship, enemy, chargedDefensiveWeapons);
    }


    //check shield state
    if(!ship.shieldComponent.isActive()) {
      //fire seconds weapons if there is no shield anymore
      List<WeaponProfile> secondaryChargedWeapons = getChargedWeaponsForCategory(ship, WeaponProfile.Category.SECONDARY);
      fireWeapons(ship, enemy, secondaryChargedWeapons);
    }
    else {
      //we are finished here and can start the next iteration
      return;
    }

    float healthPercentage = ship.healthComponent.getPercent();
    if(healthPercentage > 50) {
      List<WeaponProfile> emergencyChargedWeapons = getChargedWeaponsForCategory(ship, WeaponProfile.Category.EMERGENCY);
      fireWeapons(ship, enemy, emergencyChargedWeapons);
    }
  }

  /**
   * Updates the steering for the given ship, depending on the location to the enemy
   * @param ship the ship to update the steering for
   * @param enemy the enemy to adept the steerig for
   * @return true if addition
   */
  private void updateAttackSteering(Ship ship, Ship enemy) {
    //change steering, may be we are close enough sicne we are in the arrive steering
    if(isInAttackDistance(ship, enemy)) {
      if(ship.getDistanceTo(enemy) < ship.shipProfile.attackDistance - 100)  {
        SteeringManager.setFleeSteering(ship.steerableComponent, enemy.steerableComponent);
      }
      else {
        //increase during attack
        float radius = ship.steerableComponent.getBoundingRadius() + 100;
        SteeringManager.setFaceSteering(ship.steerableComponent, enemy.steerableComponent, radius);
      }
    }
    else {
      SteeringManager.setAttackSteering(ship.steerableComponent, enemy.steerableComponent);
    }
  }

}
