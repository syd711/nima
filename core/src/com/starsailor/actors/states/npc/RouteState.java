package com.starsailor.actors.states.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Selectable;
import com.starsailor.actors.Ship;
import com.starsailor.managers.SelectionManager;
import com.starsailor.managers.SteeringManager;

/**
 * Let the give npc follow its route.
 */
public class RouteState extends NPCState implements State<NPC> {
  @Override
  public void enter(NPC npc) {
    //deselect when in routing state
    Selectable selection = SelectionManager.getInstance().getSelection();
    if(selection != null && selection.equals(npc)) {
      SelectionManager.getInstance().setSelection(null);
    }

    Gdx.app.log(getClass().getName(), npc + " entered RouteState");
    if(npc.getShipItem().getRouteIndex() == 0) {
      SteeringManager.setRouteSteering(npc.steerableComponent, npc.routingComponent, npc.getCenter());
    }
    else {
      SteeringManager.setRouteMemberSteering(npc);
    }

  }

  @Override
  public void update(NPC npc) {
    Ship nearestEnemy = npc.findNearestEnemy(true);
    if(nearestEnemy != null && isInAttackingDistance(npc, nearestEnemy)) {
      npc.switchToBattleState(nearestEnemy);
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
