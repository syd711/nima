package com.nima.actors;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.nima.components.SteerableComponent;
import com.nima.util.GraphicsUtil;
import com.nima.util.Resources;

import static com.nima.util.Settings.MPP;
import static com.nima.util.Settings.PPM;

/**
 * A merchant spine
 */
public class Hunter extends NPC {

  public Hunter(Player player, BatchTiledMapRenderer renderer, float x, float y) {
    super(renderer, Resources.ACTOR_SPINE, Resources.ACTOR_DEFAULT_ANIMATION, 0.2f, x, y);

    Vector2 screenCenter = GraphicsUtil.getScreenCenter(getHeight());
    positionComponent.x = screenCenter.x+360;
    positionComponent.y = screenCenter.y+60;


    bodyComponent.body.setTransform(positionComponent.x*MPP, positionComponent.y*MPP, 0);
    bodyComponent.body.setLinearDamping(4f);

    speedComponent.setIncreaseBy(0.2f);
    speedComponent.setDecreaseBy(0.2f);

    Arrive<Vector2> arrive = new Arrive<>(steerableComponent, player.getComponent(SteerableComponent.class));
    arrive.setTimeToTarget(0.4f);
    arrive.setArrivalTolerance(3f);
    arrive.setDecelerationRadius(10);

    steerableComponent.setBehavior(arrive);
  }

  @Override
  public void update() {
    renderSpine();

    positionComponent.x = bodyComponent.body.getPosition().x*PPM;
    positionComponent.y = bodyComponent.body.getPosition().y*PPM;
//    rotationComponent.setRotationTarget(positionComponent.x, positionComponent.y);

//    System.out.println(bodyComponent.body.getPosition().x*PPM);
  }
}
