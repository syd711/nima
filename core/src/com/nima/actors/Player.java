package com.nima.actors;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.nima.components.ScreenPositionComponent;
import com.nima.managers.EntityClickListener;
import com.nima.util.GraphicsUtil;
import com.nima.util.Resources;

/**
 * The player with all ashley components.
 */
public class Player extends Spine implements Updateable, EntityClickListener {

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

  @Override
  public void entityClicked(Entity entity) {
    movementComponent.setTarget(entity);
  }

  @Override
  public void entityDoubleClicked(Entity entity) {

  }
}
