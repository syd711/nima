package com.starsailor.actors;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.actors.route.Route;
import com.starsailor.actors.states.StateFactory;
import com.starsailor.actors.states.npc.NPCBattleState;
import com.starsailor.actors.states.npc.NPCStates;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.RoutingComponent;
import com.starsailor.components.SelectionComponent;
import com.starsailor.components.collision.NPCCollisionComponent;
import com.starsailor.managers.SelectionManager;
import com.starsailor.model.items.ShipItem;

/**
 * Common superclass for all NPC.
 * We assume that they are instances of Spine.
 */
public class NPC extends Ship implements Selectable {
  public RoutingComponent routingComponent;
  public SelectionComponent selectionComponent;
  public NPCCollisionComponent collisionComponent;

  private State<NPC> defaultState;
  private NPCBattleState battleState;

  //not necessarily set
  private Route route;

  public NPC(ShipItem shipItem, Vector2 position) {
    super(shipItem, position);
  }

  @Override
  public void createComponents() {
    super.createComponents();

    collisionComponent = ComponentFactory.addNPCCollisionComponent(this);
    selectionComponent = ComponentFactory.addSelectionComponent(this);

    if(this.route != null) {
      routingComponent = ComponentFactory.addRoutingComponent(this, route);
    }

    //load state data
    Steering battleSteering = Steering.valueOf(shipItem.getBattleSteering().toUpperCase());
    Steering defaultSteering = Steering.valueOf(shipItem.getDefaultSteering().toUpperCase());
    //change to wandering seek and destroy if there is not route for this entity
    if(this.route == null && defaultSteering.equals(Steering.SEEK_AND_DESTROY)) {
      defaultSteering = Steering.WANDERING_SEEK_AND_DESTROY;
    }

    defaultState = StateFactory.createState(defaultSteering);
    battleState = (NPCBattleState) StateFactory.createState(battleSteering);

    statefulComponent.stateMachine.setInitialState(NPCStates.IDLE);
    switchToDefaultState();
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
    if(!destroyed && !bullet.owner.isMarkedForDestroy()) {
      switchToBattleState(bullet.owner);
    }
  }

  @Override
  protected State getDefaultState() {
    return defaultState;
  }

  @Override
  public NPCBattleState getBattleState() {
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

  public void setRoute(Route route) {
    this.route = route;
  }

  public Route getRoute() {
    return route;
  }


  // ---------------- Helper ------------------------------------------------------

  @Override
  public String toString() {
    return "NPC '" + shipItem.getName() + "' (" + shipItem.getFraction() + "/" + statefulComponent.stateMachine.getCurrentState().getClass().getSimpleName() + ")";
  }
}
