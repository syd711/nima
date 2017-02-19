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
import com.starsailor.messaging.Messages;

import java.util.List;

/**
 *
 */
public class AttackState extends NPCState implements State<NPC> {
  private List<Ship> attackingGroupMembers;

  public AttackState(Ship enemy) {
    attackingGroupMembers = enemy.formationComponent.getMembers();
  }

  @Override
  public void enter(NPC npc) {
    Gdx.app.log(getClass().getName(), npc + " entered AttackState");
    npc.setStateVisible(true);
  }

  @Override
  public void update(NPC npc) {
    //update the list of active enemies, maybe one was destroyed
    attackingGroupMembers = EntityManager.getInstance().filterAliveEntities(attackingGroupMembers);

    //check if there are more attackers first
    if(attackingGroupMembers.isEmpty()) {
      Gdx.app.log(getClass().getName(), npc + " left AttackState, because it's there are no more enemies");
      //..and return to default state
      npc.switchToDefaultState();
      return;
    }

    //there are enemies, so find the nearest one
    Ship enemy = npc.findNearestEnemyOfGroup(attackingGroupMembers);
    //..well, maybe it has been destroyed by another one
    if(enemy == null) {
      npc.switchToDefaultState();
      return;
    }

    //check if the enemy is out of range
    if(isGroupInRetreatingDistance(npc, enemy)) {//TODO never give up?
      Gdx.app.log(getClass().getName(), npc + " left AttackState, because it's in retreating distance");
      npc.switchToDefaultState();
      return;
    }


    //update steering
    updateAttackSteering(npc, enemy);

    //and shoot weapons if in attack range
    if(isInAttackingDistance(npc, enemy)) {
      fireWeapons(npc, enemy);
    }
  }

  @Override
  public void exit(NPC npc) {
    npc.setStateVisible(false);
  }

  @Override
  public boolean onMessage(NPC npc, Telegram telegram) {
    int message = telegram.message;

    switch(message) {
      case Messages.ATTACK: {
        //triggered when a bullet has hit an npc
        Bullet bullet = (Bullet) telegram.extraInfo;
        return updateStateForBullet(npc, bullet);
      }
      case Messages.ATTACKED: {
        break;
      }
    }

    return false;
  }

  //------------- Helper---------------------------------

  /**
   * Updates the list of enemies depending from whom the bullet was fired
   * @param npc the attacked npc
   * @param bullet
   * @return true if the bullet was meant for this npc
   */
  private boolean updateStateForBullet(NPC npc, Bullet bullet) {
    if(bullet.attackedMemberOf(npc)) {
      updateEnemyList(bullet, attackingGroupMembers);
      return true;
    }
    return false;
  }

  /**
   * Fires available weapons depending on the state of the ship
   *
   * @param ship  the ship that should fire the weapons and owns this state
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
   *
   * @param ship  the ship to update the steering for
   * @param enemy the enemy to adept the steerig for
   * @return true if addition
   */
  private void updateAttackSteering(Ship ship, Ship enemy) {
    //change steering, may be we are close enough sicne we are in the arrive steering
    if(isInAttackingDistance(ship, enemy)) {
      if(ship.getDistanceTo(enemy) < ship.shipProfile.attackDistance - 100) {
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
