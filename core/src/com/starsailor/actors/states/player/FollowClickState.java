package com.starsailor.actors.states.player;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.Face;
import com.badlogic.gdx.ai.steer.behaviors.LookWhereYouAreGoing;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.GameEntity;
import com.starsailor.actors.Player;
import com.starsailor.components.BodyComponent;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.SteerableComponent;
import com.starsailor.data.SteeringData;
import com.starsailor.util.box2d.Box2dUtil;

/**
 * State used for navigating the player
 */
public class FollowClickState implements State<Player> {
  private ClickTarget clickTarget;

  @Override
  public void enter(Player player) {
    if(clickTarget == null) {
      clickTarget = new ClickTarget(player.targetCoordinates);
      Arrive<Vector2> arriveSB = new Arrive<>(player.steerableComponent, clickTarget.steerableComponent);

      LookWhereYouAreGoing<Vector2> lookWhereYouAreGoingSB = new LookWhereYouAreGoing<>(player.steerableComponent)
          .setTimeToTarget(0.1f)
          .setAlignTolerance(0.01f)
          .setDecelerationRadius(MathUtils.PI);

      Face<Vector2> faceSB = new Face<Vector2>(player.steerableComponent, clickTarget.steerableComponent)
          .setTimeToTarget(0.1f)
          .setAlignTolerance(0.001f)
          .setDecelerationRadius(MathUtils.degreesToRadians * 180);

      BlendedSteering<Vector2> blendedSteering = new BlendedSteering<>(player.steerableComponent)
          .add(arriveSB, 1f)
          .add(lookWhereYouAreGoingSB, 1f);

      player.steerableComponent.setBehavior(blendedSteering);
    }
    else {
      clickTarget.update(player.targetCoordinates);
    }
  }


  @Override
  public void update(Player entity) {

  }

  @Override
  public void exit(Player entity) {

  }

  @Override
  public boolean onMessage(Player entity, Telegram telegram) {
    return false;
  }


  public class ClickTarget extends GameEntity {
    BodyComponent bodyComponent;
    SteerableComponent steerableComponent;

    ClickTarget(Vector2 worldCoordinates) {
      bodyComponent = ComponentFactory.addBodyComponent(this, Box2dUtil.clickBody(worldCoordinates));
      steerableComponent = ComponentFactory.addSteerableComponent(this, bodyComponent.body, new SteeringData());
      ComponentFactory.addPlayerTargetCollisionComponent(this);
    }

    void update(Vector2 worldCoordinates) {
      bodyComponent.setWorldPosition(worldCoordinates);
    }
  }
}
