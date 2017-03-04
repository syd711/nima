package com.starsailor.actors;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.actors.states.StateFactory;
import com.starsailor.actors.states.npc.BattleState;
import com.starsailor.actors.states.npc.NPCStates;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.RoutingComponent;
import com.starsailor.components.SelectionComponent;
import com.starsailor.components.collision.NPCCollisionComponent;
import com.starsailor.managers.EntityManager;
import com.starsailor.managers.SelectionManager;
import com.starsailor.model.items.ShipItem;

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
  private BattleState battleState;

  //not necessarily set
  private com.starsailor.actors.route.Route route;

  public NPC(ShipItem shipItem, Vector2 position) {
    super(shipItem, position);

    Steering defaultSteering = Steering.valueOf(shipItem.getDefaultSteering().toUpperCase());
    Steering battleSteering = Steering.valueOf(shipItem.getBattleSteering().toUpperCase());

    defaultState = StateFactory.createState(defaultSteering);
    battleState = (BattleState) StateFactory.createState(battleSteering);
  }

  @Override
  public void createComponents(Fraction fraction) {
    super.createComponents(fraction);

    collisionComponent = ComponentFactory.addNPCCollisionComponent(this);
    selectionComponent = ComponentFactory.addSelectionComponent(this);

    if(this.route != null) {
      routingComponent = ComponentFactory.addRoutingComponent(this, route);
      formationComponent = ComponentFactory.addFormationComponent(this, steerableComponent, shipData.getDistanceData().getFormationDistance());
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

  /**
   * Switches this entity to the attacked state if not already there.
   *
   * @param enemy the enemy ship that has been fired or detected
   */
  public void switchToBattleState(Ship enemy) {
    if(isInDefaultState()) {
      //update the battle state since it is only updated, not recreated
      getBattleState().updateEnemyList(enemy);
      //then switch to the state...
      getStateMachine().changeState(getBattleState());

      //...and notify all members that 'we' are attacked
      List<Ship> groupMembers = formationComponent.getMembers();
      for(Ship formationMember : groupMembers) {
        if(!formationMember.equals(this)) {
          ((NPC) formationMember).switchToBattleState(enemy);
        }
      }
    }
    else if(isInBattleState()) {
      getBattleState().updateEnemyList(enemy);
      List<Ship> groupMembers = formationComponent.getMembers();
      for(Ship formationMember : groupMembers) {
        if(!formationMember.equals(this)) {
          formationMember.getBattleState().updateEnemyList(enemy);
        }
      }
    }
  }

  @Override
  public void applyDamageFor(Bullet bullet) {
    boolean destroyed = updateDamage(bullet);
    if(!destroyed && !bullet.owner.isMarkedForDestroy()) {
      switchToBattleState(bullet.owner);
    }
  }

  @Override
  protected State getDefaultState() {
    return defaultState;
  }

  @Override
  public BattleState getBattleState() {
    return battleState;
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

  public void setRoute(com.starsailor.actors.route.Route route) {
    this.route = route;
  }

  // ---------------- Helper ------------------------------------------------------


  @Override
  public String toString() {
    return "NPC '" + shipItem.getName() + "' (" + shipItem.getFraction() + "/" + getStateMachine().getCurrentState().getClass().getSimpleName() + ")";
  }
}
