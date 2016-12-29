package com.nima.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.nima.util.GraphicsUtil;
import com.nima.util.Resources;

/**
 * A merchant spine
 */
public class Merchant extends Spine {

  public Merchant(Player player, BatchTiledMapRenderer renderer) {
    super(renderer, Resources.ACTOR_SPINE, Resources.ACTOR_DEFAULT_ANIMATION, 0.2f);

    Vector2 screenCenter = GraphicsUtil.getScreenCenter(getHeight());
    positionComponent.x = screenCenter.x+360;
    positionComponent.y = screenCenter.y+60;

    speedComponent.setIncreaseBy(0.2f);
    speedComponent.setDecreaseBy(0.2f);

    Arrive<Vector2> arrive = new Arrive<>(steerable, player.steerable);
    steerable.setBehavior(arrive);
  }


  @Override
  public void update() {
    super.update();

    steerable.update(Gdx.graphics.getDeltaTime());
  }
}
