package com.nima.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.nima.ai.SpineSteerable;
import com.nima.util.GraphicsUtil;
import com.nima.util.Resources;
import com.nima.util.Settings;

/**
 * A merchant spine
 */
public class Merchant extends Spine {
  protected SpineSteerable steerable;
  private final Body body;


  public Merchant(World world, Player player, BatchTiledMapRenderer renderer) {
    super(renderer, Resources.ACTOR_SPINE, Resources.ACTOR_DEFAULT_ANIMATION, 0.2f);

    Vector2 screenCenter = GraphicsUtil.getScreenCenter(getHeight());
    positionComponent.x = screenCenter.x+360;
    positionComponent.y = screenCenter.y+60;

    speedComponent.setIncreaseBy(0.2f);
    speedComponent.setDecreaseBy(0.2f);



    //box2d
    BodyDef def = new BodyDef();
    def.type = BodyDef.BodyType.DynamicBody;
    def.fixedRotation = false;
    def.position.set(positionComponent.x, positionComponent.y);
    body = world.createBody(def);

    PolygonShape shape = new PolygonShape();
    //calculated from center!
    shape.setAsBox(skeleton.getData().getWidth()*0.2f / 2 / Settings.PPM, skeleton.getData().getHeight()*0.2f / 2 / Settings.PPM);
    body.createFixture(shape, 0f);
    shape.dispose();

    //AI
    steerable = new SpineSteerable(this, body,500);

    Arrive<Vector2> arrive = new Arrive<>(steerable, player.steerable);
//    Evade<Vector2> arrive = new Evade<>(steerable, player.steerable);
    arrive.setTimeToTarget(0.01f);
    arrive.setArrivalTolerance(2f);
    arrive.setDecelerationRadius(10);
//    arrive.setEnabled(true);

    steerable.setBehavior(arrive);
  }


  @Override
  public void update() {
    super.update();

    steerable.update(Gdx.graphics.getDeltaTime());

    positionComponent.x = body.getPosition().x;
    positionComponent.y = body.getPosition().y;
  }
}
