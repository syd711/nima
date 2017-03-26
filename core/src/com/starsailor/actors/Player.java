package com.starsailor.actors;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.actors.states.npc.NPCBattleState;
import com.starsailor.actors.states.player.PlayerStates;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.ScreenPositionComponent;
import com.starsailor.managers.EntityManager;
import com.starsailor.managers.SelectionManager;
import com.starsailor.model.items.ShipItem;
import com.starsailor.ui.UIManager;

import java.util.Collections;
import java.util.List;

/**
 * The player with all ashley components.
 */
public class Player extends Ship implements IFormationOwner<Ship> {
  private static Player instance = null;

  private Entity target;
  private Vector2 targetCoordinates;

  private boolean inBattleState = false;

  public static Player getInstance() {
    return instance;
  }

  public Player(ShipItem shipItem, Vector2 position) {
    super(shipItem, position);
    instance = this;
  }

  @Override
  public void createComponents() {
    super.createComponents();
    ComponentFactory.addPlayerCollisionComponent(this);
    add(new ScreenPositionComponent(0, 0));
  }

  /**
   * The player entity is updated itself beside the entity manager, system or states
   */
  public void update() {
    Selectable selection = SelectionManager.getInstance().getSelection();
    if(selection != null) {
      Ship ship = (Ship) selection;
      if(isInBattleState() && !ship.isMarkedForDestroy() && isInRetreatingDistance(ship)) {
        switchToDefaultState();
      }
    }
  }

  public Entity getTarget() {
    return target;
  }

  public void setTarget(Entity target) {
    this.target = null;
  }

  @Override
  protected State getDefaultState() {
    return null;
  }

  @Override
  protected NPCBattleState getBattleState() {
    return null;
  }

  @Override
  public void switchToBattleState(Ship enemy) {
    inBattleState = true;
    shieldSpineComponent.setEnabled(true);
    bodyShipComponent.setTargetRadius(shipData.getBodyData().getRadius()*shieldSpineComponent.getJsonScaling());
    UIManager.getInstance().getHudStage().getWeaponsPanel().activate();
  }

  @Override
  public void switchToDefaultState() {
    inBattleState = false;
    shieldSpineComponent.setEnabled(false);
    bodyShipComponent.setTargetRadius(shipData.getBodyData().getRadius()/2*shieldSpineComponent.getJsonScaling());
    UIManager.getInstance().getHudStage().getWeaponsPanel().deactivate();
  }

  @Override
  public boolean isInBattleState() {
    return inBattleState;
  }

  @Override
  public void applyDamageFor(Bullet bullet) {
    updateDamage(bullet);
//    if(SelectionManager.getInstance().getSelection() == null) {
//      SelectionManager.getInstance().setSelection((Selectable) bullet.owner);
//    }
  }

  /**
   * Moves to the given world coordinates
   * @param worldCoordinates the coordinates the user has clicked at
   */
  public void moveTo(Vector2 worldCoordinates) {
    targetCoordinates = worldCoordinates;
    Entity possibleTarget = EntityManager.getInstance().getEntityAt(worldCoordinates);
    //ships are not targets
    this.target = null;
    if(possibleTarget instanceof Location) {
      target = possibleTarget;
    }
    changeState(PlayerStates.FOLLOW_CLICK);
    steerableComponent.setEnabled(true);
  }

  public Vector2 getTargetCoordinates() {
    return targetCoordinates;
  }

  // IFormation owner interface implementation --------------------------------------

  @Override
  public List<Ship> getMembers() {
    return Collections.emptyList();
  }

  @Override
  public void addMember(Ship ship) {
    //TODO
  }

  @Override
  public void removeMember(Ship member) {
    //TODO
  }

  @Override
  public float getMaxMemberDistance() {
    return 0;
  }
}
