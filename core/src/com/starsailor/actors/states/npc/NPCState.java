package com.starsailor.actors.states.npc;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Ship;
import com.starsailor.components.SpineComponent;
import com.starsailor.managers.EntityManager;

/**
 * Created by Matthias on 07.02.2017.
 */
public class NPCState {

  /**
   * Searches for an enemy to shoot at.
   * The entities "attackDistance" is used for this which means
   * that the ship itself has not necessarily a weapon in shooting range.
   *
   * @return True if an enemy was found to shoot at
   */
  public boolean findAndLockNearestTarget(NPC npc) {
    //TODO filter for friends!
    float attackDistance = npc.shipProfile.attackDistance;
    Ship nearestNeighbour = findNearestNeighbour(npc);
    if(nearestNeighbour != null) {
      float distanceToEnemy = nearestNeighbour.getDistanceTo(npc);
      if(distanceToEnemy != 0 && distanceToEnemy < attackDistance) {
        npc.setShieldEnabled(true);
        npc.lockTarget(nearestNeighbour);
        return true;
      }
    }
    return false;
  }

  public Ship findNearestNeighbour(NPC npc) {
    Ship nearestNeighbour = null;
    //TODO not necessarily a spine
    ImmutableArray<Entity> entitiesFor = EntityManager.getInstance().getEntitiesFor(SpineComponent.class);
    for(Entity entity : entitiesFor) {
      Ship ship = (Ship) entity;
      if(ship.equals(npc)) {
        continue;
      }
      if(nearestNeighbour == null) {
        nearestNeighbour = ship;
        continue;
      }

      if(npc.getDistanceTo(ship) < npc.getDistanceTo(nearestNeighbour)) {
        nearestNeighbour = ship;
      }
    }
    return nearestNeighbour;
  }
}
