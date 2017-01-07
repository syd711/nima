package com.nima.actors;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nima.Game;
import com.nima.components.ComponentFactory;
import com.nima.components.MapObjectComponent;
import com.nima.components.ScreenPositionComponent;
import com.nima.components.ShootingComponent;
import com.nima.managers.CollisionListener;
import com.nima.managers.EntityManager;
import com.nima.managers.GameStateManager;
import com.nima.systems.LightSystem;
import com.nima.util.Box2dUtil;
import com.nima.util.GraphicsUtil;
import com.nima.util.Resources;
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

  private static Player instance = null;

  public static Player getInstance() {
    return instance;
  }

  public Player() {
    super(Resources.ACTOR_SPINE, Resources.ACTOR_DEFAULT_ANIMATION, 0.3f);

    speedComponent.setIncreaseBy(velocityUp);
    speedComponent.setDecreaseBy(velocityDown);

    Vector2 screenCenter = GraphicsUtil.getScreenCenter(getHeight());
    add(new ScreenPositionComponent(screenCenter.x, screenCenter.y));

    shootingComponent = ComponentFactory.addShootableComponent(this);

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

  private void moveTo(float x, float y) {
    movementComponent.moveTo(x, y);
    //...then the speed to travel with
    Vector2 point1 = new Vector2(positionComponent.x, positionComponent.y);
    Vector2 point2 = new Vector2(x, y);
    speedComponent.calculateTargetSpeed(point1, point2);
  }

  public void fireAt(Vector2 worldCoordinates) {
    int bulletDelayMillis = 350;
    long lastBulletTime = shootingComponent.lastBulletTime;

    if(Game.currentTimeMillis - lastBulletTime > bulletDelayMillis) {
      Bullet bullet = Bullet.newBullet(positionComponent.getPosition());

      Vector2 from = Box2dUtil.toBox2Vector(positionComponent.getPosition());
      Vector2 to = Box2dUtil.toBox2Vector(worldCoordinates);
      float radianAngle =  Box2dUtil.getBox2dAngle(from ,to);
      System.out.println();
      float angle = (float) Math.toDegrees(radianAngle);

      Body bulletBody = bullet.bodyComponent.body;
      com.badlogic.gdx.graphics.g2d.Sprite sprite = bullet.spriteComponent.sprite;
      bulletBody.setTransform(bulletBody.getPosition().x, bulletBody.getPosition().y, radianAngle);

      float mXDir= -(float) Math.cos(angle*Math.PI/180);
      float mYDir= -(float) Math.sin(angle*Math.PI/180);

      float speedFactor = 15f;
      Vector2 impulse = new Vector2(speedFactor*mXDir / Settings.PPM, speedFactor*mYDir / Settings.PPM);
//      impulse.mul(2);

//      float xImpulse = (float) -Math.toRadians(Math.toDegrees(angle)+90);
//      Vector2 impulse = new Vector2(0.004f * (float) Math.sin(xImpulse),
//          0.004f * (float) Math.cos(angle));
      bulletBody.applyLinearImpulse(impulse, bulletBody.getPosition(), true);
      sprite.setRotation((float) Math.toDegrees(radianAngle));

      shootingComponent.lastBulletTime = Game.currentTimeMillis;

      EntityManager.getInstance().add(bullet);
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
