package com.nima.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.Evade;
import com.badlogic.gdx.math.Vector2;
import com.nima.actors.NPC;
import com.nima.actors.Player;
import com.nima.components.SteerableComponent;

/**
 *
 */
public class EvadePlayerState implements State<NPC> {
  @Override
  public void enter(NPC npc) {
    SteerableComponent steerableComponent = npc.getComponent(SteerableComponent.class);
    Evade<Vector2> evade = new Evade<>(steerableComponent, Player.getInstance().getComponent(SteerableComponent.class));
    steerableComponent.setBehavior(evade);
  }

  @Override
  public void update(NPC npc) {

  }

  @Override
  public void exit(NPC npc) {

  }

  @Override
  public boolean onMessage(NPC npc, Telegram telegram) {
    return false;
  }
}
