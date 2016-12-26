package com.nima.actors;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.nima.components.ScreenPositionComponent;
import com.nima.managers.EntityManager;
import com.nima.util.GraphicsUtil;
import com.nima.util.Resources;

/**
 * The player with all ashley components.
 */
public class Player extends Spine implements Updateable {

  public Player(World world, RayHandler rayHandler) {
    super(Resources.ACTOR_SPINE, Resources.ACTOR_DEFAULT_ANIMATION, 0.3f);

    Vector2 screenCenter = GraphicsUtil.getScreenCenter(dimensionComponent.height);
    positionComponent.x = screenCenter.x;
    positionComponent.y = screenCenter.y;

    add(new ScreenPositionComponent(screenCenter.x, screenCenter.y));
  }

  @Override
  public void update() {
  }

  public void moveTo(Vector2 worldCoordinates) {
    //first update the target to move to...
    Entity target = EntityManager.getInstance().getEntityAt(worldCoordinates.x, worldCoordinates.y);
    if(target != null) {
      movementComponent.moveToEntity(target);
    }
    else {
      movementComponent.moveTo(worldCoordinates.x, worldCoordinates.y);
    }

    //...then the speed to travel with
    Vector2 point1 = new Vector2(positionComponent.x, positionComponent.y);
    Vector2 point2 = new Vector2(worldCoordinates.x, worldCoordinates.y);
    speedComponent.calculateTargetSpeed(point1, point2);
  }
}
