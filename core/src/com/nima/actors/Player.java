package com.nima.actors;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.nima.components.ScreenPositionComponent;
import com.nima.managers.CollisionListener;
import com.nima.managers.EntityManager;
import com.nima.managers.GameStateManager;
import com.nima.util.GraphicsUtil;
import com.nima.util.Resources;

/**
 * The player with all ashley components.
 */
public class Player extends Spine implements Updateable, CollisionListener {

  private Entity targetEntity;

  public Player(BatchTiledMapRenderer renderer, World world, RayHandler rayHandler) {
    super(renderer, Resources.ACTOR_SPINE, Resources.ACTOR_DEFAULT_ANIMATION, 0.3f);

    Vector2 screenCenter = GraphicsUtil.getScreenCenter(getHeight());
    positionComponent.x = screenCenter.x;
    positionComponent.y = screenCenter.y;

    add(new ScreenPositionComponent(screenCenter.x, screenCenter.y));
  }

  @Override
  public void update() {
    super.update();

    if(targetEntity != null && scalingComponent.getCurrentScaling() == 0.4f) {
      GameStateManager.getInstance().setNavigating(false);
      GameStateManager.getInstance().setInGameMenu(true);
    }
  }

  public void moveTo(Vector2 worldCoordinates) {
    //first update the target to move to...
    targetEntity = EntityManager.getInstance().getEntityAt(worldCoordinates.x, worldCoordinates.y);
    if(targetEntity != null) {
      movementComponent.moveToEntity(targetEntity);
    }
    else {
      movementComponent.moveTo(worldCoordinates.x, worldCoordinates.y);
    }

    //...then the speed to travel with
    Vector2 point1 = new Vector2(positionComponent.x, positionComponent.y);
    Vector2 point2 = new Vector2(worldCoordinates.x, worldCoordinates.y);
    speedComponent.calculateTargetSpeed(point1, point2);
  }

  //------------------ Collision Listener --------------------------------

  @Override
  public void collisionStart(Player player, Entity mapObjectEntity) {
    if(targetEntity != null && targetEntity.equals(mapObjectEntity)) {
      System.out.println("Reached destination");
      scalingComponent.setScaling(0.4f);
    }
  }

  @Override
  public void collisionEnd(Player player, Entity mapObjectEntity) {
    if(scalingComponent.getScaling() < 1f) {
      scalingComponent.setScaling(1f);
    }
  }

  @Override
  public void collisionStart(Spine spine, Entity mapObjectEntity) {

  }

  @Override
  public void collisionEnd(Spine spine, Entity mapObjectEntity) {

  }
}
