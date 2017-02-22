package com.starsailor.actors.states.player;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.Player;
import com.starsailor.components.MapObjectComponent;

/**
 *
 */
public class MoveToStationState implements State<Player> {
  @Override
  public void enter(Player player) {
    MapObjectComponent mapObjectComponent = player.target.getComponent(MapObjectComponent.class);
    Vector2 centeredPosition = mapObjectComponent.getCenteredPosition();
    player.moveTo(centeredPosition);
  }

  @Override
  public void update(Player player) {

  }

  @Override
  public void exit(Player player) {

  }

  @Override
  public boolean onMessage(Player player, Telegram telegram) {
    return false;
  }
}
