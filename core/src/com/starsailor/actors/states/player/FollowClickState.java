package com.starsailor.actors.states.player;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.ClickTarget;
import com.starsailor.actors.Player;
import com.starsailor.actors.SpineShipAnimations;
import com.starsailor.components.MapObjectComponent;
import com.starsailor.components.SpineShipComponent;
import com.starsailor.managers.SteeringManager;
import com.starsailor.ui.UIManager;

/**
 * State used for navigating the player
 */
public class FollowClickState implements State<Player> {

  @Override
  public void enter(Player player) {
    SpineShipComponent spineComponent = player.getComponent(SpineShipComponent.class);
    spineComponent.setAnimation(SpineShipAnimations.Move);

    ClickTarget.getInstance().update(player.targetCoordinates);

    //check if a target is selected, update to the center then
    if(player.getTarget() != null) {
      MapObjectComponent mapObjectComponent = player.getTarget().getComponent(MapObjectComponent.class);
      Vector2 centeredPosition = mapObjectComponent.getCenteredPosition();
      ClickTarget.getInstance().update(centeredPosition);
      UIManager.getInstance().getHudStage().getNavigatorPanel().activate();
    }
    else {
      UIManager.getInstance().getHudStage().getNavigatorPanel().deactivate();
    }

    SteeringManager.setFollowTargetSteering(player.steerableComponent, ClickTarget.getInstance().getSteerableComponent());
  }


  @Override
  public void update(Player player) {

  }

  @Override
  public void exit(Player player) {
    SpineShipComponent spineComponent = player.getComponent(SpineShipComponent.class);
    spineComponent.setAnimation(SpineShipAnimations.Stand);
    ClickTarget.getInstance().hide();
  }

  @Override
  public boolean onMessage(Player player, Telegram telegram) {
    return false;
  }
}
