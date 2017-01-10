package com.nima.actors;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.nima.components.*;
import com.nima.data.DataEntities;
import com.nima.managers.CollisionListener;
import com.nima.managers.EntityManager;
import com.nima.managers.GameStateManager;
import com.nima.systems.LightSystem;
import com.nima.util.GraphicsUtil;
import com.nima.util.Settings;

/**
 * The player with all ashley components.
 */
public class Player extends Spine implements Updateable, CollisionListener {
  public float velocityUp = 0.03f;
  public float velocityDown = 0.03f;

  private Entity targetEntity;
  private boolean dockingProcedure = false;

  private ShootingComponent shootingComponent;
  protected MovementComponent movementComponent;

  private static Player instance = null;

  public static Player getInstance() {
    return instance;
  }

  public Player() {
    super(DataEntities.getShip(DataEntities.SHIP_PLAYER));

    speedComponent.setIncreaseBy(velocityUp);
    speedComponent.setDecreaseBy(velocityDown);

    Vector2 screenCenter = GraphicsUtil.getScreenCenter(getHeight());
    add(new ScreenPositionComponent(screenCenter.x, screenCenter.y));

    shootingComponent = ComponentFactory.addShootableComponent(this);
    shootingComponent.weaponProfile = DataEntities.getWeapon(DataEntities.WEAPON_LASER);
    movementComponent = ComponentFactory.addMovementComponent(this);

    instance = this;
  }

  @Override
  public void update() {
    super.update();

    if(dockingProcedure) {
      LightSystem lightSystem = EntityManager.getInstance().getLightSystem();
      if(lightSystem.isOutFaded()) {
        GameStateManager.getInstance().enterStationMode();
      }
    }
  }

  /**
   * Applying the input the user has inputted.
   *
   * @param worldCoordinates
   */
  public void setTargetCoordinates(Vector2 worldCoordinates) {
    if(dockingProcedure) {
      return;
    }

    //first update the target to move to...
    float targetX = worldCoordinates.x;
    float targetY = worldCoordinates.y;

    targetEntity = EntityManager.getInstance().getEntityAt(worldCoordinates.x, worldCoordinates.y);
    if(targetEntity != null) {
      MapObjectComponent mapObjectComponent = targetEntity.getComponent(MapObjectComponent.class);
      Vector2 centeredPosition = mapObjectComponent.getCenteredPosition();
      targetX = centeredPosition.x;
      targetY = centeredPosition.y;
    }

    moveTo(targetX, targetY);
  }

  public void moveTo(float x, float y) {
    rotationComponent.setRotationTarget(x, y);

    //...then the speed to travel with
    Vector2 point1 = new Vector2(positionComponent.x, positionComponent.y);
    Vector2 point2 = new Vector2(x, y);
    speedComponent.applyTargetSpeed(point1, point2);
  }

  /**
   * Fires a bullet using the active weapon profile
   * @param worldCoordinates the target to shoot to
   */
  public void fireAt(Vector2 worldCoordinates) {
    if(shootingComponent.isCharged()) {
      Bullet.fireBullet(shootingComponent, positionComponent.getPosition(), worldCoordinates);
    }
  }

  //------------------ Collision Listener --------------------------------

  @Override
  public void collisionStart(Player player, Entity mapObjectEntity) {
    if(targetEntity != null && targetEntity.equals(mapObjectEntity)) {
      enterStation();
    }
  }

  @Override
  public void collisionEnd(Player player, Entity mapObjectEntity) {

  }

  @Override
  public void collisionStart(Spine spine, Entity mapObjectEntity) {

  }

  @Override
  public void collisionEnd(Spine spine, Entity mapObjectEntity) {

  }

  //--------------- Event execution-----------------------------

  private void enterStation() {
    dockingProcedure = true;
    speedComponent.setTargetValue(1.5f);
    scalingComponent.setTargetValue(Settings.DOCKING_TARGET_SCALE);

    LightSystem lightSystem = EntityManager.getInstance().getLightSystem();
    lightSystem.fadeOut(true);
  }

  public void leaveStation() {
    scalingComponent.setTargetValue(1f);
    LightSystem lightSystem = EntityManager.getInstance().getLightSystem();
    lightSystem.fadeOut(false);
    speedComponent.setTargetValue(0f);
    dockingProcedure = false;
    targetEntity = null;
    rotationComponent.setRotationTarget(positionComponent.x + 100, positionComponent.y);
  }


}
