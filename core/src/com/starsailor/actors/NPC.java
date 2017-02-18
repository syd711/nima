package com.starsailor.actors;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.actors.states.npc.*;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.RoutingComponent;
import com.starsailor.components.SelectionComponent;
import com.starsailor.components.collision.NPCCollisionComponent;
import com.starsailor.data.ShipProfile;
import com.starsailor.managers.SelectionManager;

import java.util.List;

/**
 * Common superclass for all NPC.
 * We assume that they are instances of Spine.
 */
public class NPC extends Ship implements Selectable {
  public RoutingComponent routingComponent;
  public SelectionComponent selectionComponent;
  public NPCCollisionComponent collisionComponent;

  private State<NPC> defaultState;

  //not necessarily set
  private Route route;

  public NPC(ShipProfile profile, State<NPC> defaultState, Vector2 position) {
    super(profile, position);
    this.shipProfile = profile;
    this.defaultState = defaultState;
  }

  @Override
  public void createComponents(Fraction fraction) {
    super.createComponents(fraction);
    collisionComponent = ComponentFactory.addNPCCollisionComponent(this);
    selectionComponent = ComponentFactory.addSelectionComponent(this);

    if(this.route != null) {
      routingComponent = ComponentFactory.addRoutingComponent(this, route);
    }

    getStateMachine().setInitialState(NPCStates.IDLE);
  }

  @Override
  public void switchToDefaultState() {
    super.switchToDefaultState();
    if(selectionComponent.isActive()) {
      SelectionManager.getInstance().setSelection(null);
    }
  }

  @Override
  public void applyDamageFor(Bullet bullet) {
    boolean destroyed = updateDamage(bullet);
    //player is also a ship, so we skip here
    if(!destroyed) {
      moveToBattleState();
      updateAttackState(bullet);
    }
  }

  @Override
  protected State getDefaultState() {
    return defaultState;
  }

  @Override
  public SelectionComponent getSelectionComponent() {
    return selectionComponent;
  }

  @Override
  public void setSelected(boolean b) {
    selectionComponent.setActive(b);
  }

  @Override
  public boolean isSelected() {
    return selectionComponent.isActive();
  }

  public void setRoute(Route route) {
    this.route = route;
  }

  // ---------------- Helper ------------------------------------------------------

  public void switchToBattleState() {
    ShipProfile.Types type = shipProfile.getType();
    switch(type) {
      case MERCHANT: {
        getStateMachine().changeState(new FleeFromAttackerAndWaitState());
        break;
      }
      case CRUSADER: {
        getStateMachine().changeState(new AttackState());
        break;
      }
      case PIRATE: {
        getStateMachine().changeState(new AttackState());
        break;
      }
      default: {
        getStateMachine().changeState(new AttackState());
        break;
      }
    }
  }

  /**
   * Switches this entity to the attacked state if not already there.
   */
  public void moveToBattleState() {
    if(isInDefaultState()) {
      switchToBattleState();

      //notify all members that 'we' are attacked
      List<Ship> groupMembers = formationComponent.getMembers();
      for(Ship formationMember : groupMembers) {
        ((NPC)formationMember).moveToBattleState();
      }
    }
  }

  private State createDefaultState() {
    ShipProfile.Types type = shipProfile.getType();
    switch(type) {
      case PIRATE: {
        return new RoutedSeekAndDestroyState();
      }
      case CRUSADER: {
        return new GuardState();
      }
      default: {
        return new RouteState();
      }
    }
  }

  /**
   * Updates the last bullet for the attack state
   *
   * @param bullet the attacker bullet
   */
  private void updateAttackState(Bullet bullet) {
    State currentState = getStateMachine().getCurrentState();
    if(currentState instanceof AttackState) {
      ((AttackState) getStateMachine().getCurrentState()).hitBy(bullet);
    }
  }

  @Override
  public String toString() {
    return "NPC '" + shipProfile.name + "'";
  }
}
