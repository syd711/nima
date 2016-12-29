package com.nima.actors;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.nima.ai.SpineSteerable;
import com.nima.components.MapObjectComponent;
import com.nima.components.ScreenPositionComponent;
import com.nima.managers.CollisionListener;
import com.nima.managers.EntityManager;
import com.nima.managers.GameStateManager;
import com.nima.systems.LightSystem;
import com.nima.util.GraphicsUtil;
import com.nima.util.Resources;
import com.nima.util.Settings;

/**
 * The player with all ashley components.
 */
public class Player extends Spine implements Updateable, CollisionListener {
  public final SpineSteerable steerable;
  public float velocityUp = 0.03f;
  public float velocityDown = 0.03f;

  private Entity targetEntity;
  private boolean dockingProcedure = false;
  public final Body body;

  public Player(BatchTiledMapRenderer renderer, World world, RayHandler rayHandler) {
    super(renderer, Resources.ACTOR_SPINE, Resources.ACTOR_DEFAULT_ANIMATION, 0.3f);

    Vector2 screenCenter = GraphicsUtil.getScreenCenter(getHeight());
    positionComponent.x = screenCenter.x;
    positionComponent.y = screenCenter.y;

    speedComponent.setIncreaseBy(velocityUp);
    speedComponent.setDecreaseBy(velocityDown);

    BodyDef def = new BodyDef();
    def.type = BodyDef.BodyType.DynamicBody;
    def.fixedRotation = false;
    def.position.set(positionComponent.x, positionComponent.y);
    body = world.createBody(def);

    PolygonShape shape = new PolygonShape();
    //calculated from center!
    shape.setAsBox(skeleton.getData().getWidth() * 0.2f / 2 / Settings.PPM, skeleton.getData().getHeight() * 0.2f / 2 / Settings.PPM);
    body.createFixture(shape, 0f);
    shape.dispose();

    //AI
    steerable = new SpineSteerable(this, body,500);

    add(new ScreenPositionComponent(screenCenter.x, screenCenter.y));
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

    body.setTransform(getCenter().x, getCenter().y, 0);

    steerable.update(Gdx.graphics.getDeltaTime());
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
