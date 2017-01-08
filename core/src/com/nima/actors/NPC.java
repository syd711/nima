package com.nima.actors;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.math.Vector2;
import com.nima.components.RoutingComponent;
import com.nima.components.ShootingComponent;
import com.nima.data.RouteProfile;
import com.nima.managers.EntityManager;
import com.nima.systems.states.AttackState;
import com.nima.util.GraphicsUtil;

import static com.nima.util.Settings.MPP;

/**
 * Common superclass for all NPC.
 * We assume that they are instances of Spine.
 */
public class NPC extends Spine {

  public StateMachine<NPC, AttackState> attackStateMachine;

  public RoutingComponent routingComponent;

  public NPC(Player player, String path, String defaultAnimation, float jsonScaling, float x, float y) {
    super(path, defaultAnimation, jsonScaling, x, y);

    Vector2 screenCenter = GraphicsUtil.getScreenCenter(getHeight());
    positionComponent.x = screenCenter.x + 360;
    positionComponent.y = screenCenter.y + 60;

    bodyComponent.body.setTransform(positionComponent.x * MPP, positionComponent.y * MPP, 0);
    bodyComponent.body.setLinearDamping(4f);

    speedComponent.setIncreaseBy(0.2f);
    speedComponent.setDecreaseBy(0.2f);


    add(new ShootingComponent());

    this.attackStateMachine = new DefaultStateMachine<>(this, AttackState.SLEEP);
  }

  //routing npc
  public NPC(RouteProfile route, String path, String defaultAnimation, float jsonScaling, float x, float y) {
    super(path, defaultAnimation, jsonScaling, x, y);
    //TODO ranomize route
    Vector2 target = route.coordinates.values().iterator().next();

    bodyComponent.body.setTransform(target.x * MPP, target.y * MPP, 0);
    bodyComponent.body.setLinearDamping(4f);

    speedComponent.setIncreaseBy(0.2f);
    speedComponent.setDecreaseBy(0.2f);

    add(new ShootingComponent());

    routingComponent = new RoutingComponent();
    routingComponent.applyRoute(route);
    add(routingComponent);

    EntityManager.getInstance().addUpdateable(this);
  }

  @Override
  public void update() {
    super.update();
    if(this.attackStateMachine != null) {
      this.attackStateMachine.update();
    }

    if(routingComponent != null) {
      float angle = GraphicsUtil.getAngle(positionComponent.getPosition(), routingComponent.target);
      rotationComponent.setRotationTarget(angle);
//      Vector2 updatedCoordinates = GraphicsUtil.getUpdatedCoordinates(angle, 4f);
//      float box2dAngle = Box2dUtil.getBox2dAngle(positionComponent.getPosition(), routingComponent.target);
//      bodyComponent.body.setTransform(updatedCoordinates.x, updatedCoordinates.y, box2dAngle);
    }
  }
}
