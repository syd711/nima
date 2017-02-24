package com.starsailor.actors;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.actors.states.npc.*;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.RoutingComponent;
import com.starsailor.components.SelectionComponent;
import com.starsailor.components.collision.NPCCollisionComponent;
import com.starsailor.data.ShipData;
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

  public NPC(String name, ShipData profile, State<NPC> defaultState, Vector2 position) {
    super(name, profile, position);
    this.shipData = profile;
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
      moveToBattleState(bullet.owner);
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

  public void switchGroupToBattleState(Ship enemy) {
    List<Ship> members = formationComponent.getMembers();
    for(Ship member : members) {
      switchToBattleState(member, enemy);
    }
  }

  private void switchToBattleState(Ship ship, Ship enemy) {
    ShipData.Types type = ship.shipData.getType();
    StateMachine stateMachine = ship.getStateMachine();
    switch(type) {
      case MERCHANT: {
        stateMachine.changeState(new FleeFromAttackerAndWaitState(enemy));
        break;
      }
      case CRUSADER: {
        stateMachine.changeState(new AttackState(enemy));
        break;
      }
      case PIRATE: {
        stateMachine.changeState(new AttackState(enemy));
        break;
      }
      default: {
        stateMachine.changeState(new AttackState(enemy));
        break;
      }
    }
  }

  /**
   * Switches this entity to the attacked state if not already there.
   * @param owner
   */
  public void moveToBattleState(Ship owner) {
    if(isInDefaultState()) {
      switchGroupToBattleState(owner);

      //notify all members that 'we' are attacked
      List<Ship> groupMembers = formationComponent.getMembers();
      for(Ship formationMember : groupMembers) {
        ((NPC)formationMember).moveToBattleState(owner);
      }
    }
  }

  @Override
  public String toString() {
    return "NPC '" + name + "' (" + shipData.getName() + "/" + getStateMachine().getCurrentState().getClass().getSimpleName() + ")";
  }
}
