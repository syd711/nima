package com.nima.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.math.Vector2;
import com.nima.actors.NPC;
import com.nima.actors.Player;
import com.nima.actors.states.NPCStates;
import com.nima.components.SteerableComponent;
import com.nima.systems.behaviours.FaceToPlayerBehaviour;

/**
 *
 */
public class PursueAndShootPlayerState implements State<NPC> {
  @Override
  public void enter(NPC npc) {
    SteerableComponent steerableComponent = npc.getComponent(SteerableComponent.class);
    Pursue<Vector2> behaviour = new Pursue<>(steerableComponent, Player.getInstance().getComponent(SteerableComponent.class));
    behaviour.setMaxPredictionTime(0.7f);
    steerableComponent.setBehavior(behaviour);
    steerableComponent.setFaceBehaviour(new FaceToPlayerBehaviour(npc));
  }

  @Override
  public void update(NPC npc) {
    float distanceToPlayer = npc.distanceToPlayer();
    if(distanceToPlayer > npc.shipProfile.attackDistance) {
      npc.getStateMachine().changeState(NPCStates.ROUTE);
    }

    if(npc.shootingComponent.isCharged()) {
      Vector2 shootAt = Player.getInstance().getCenter();
      npc.fireAt(shootAt);
    }
  }

  @Override
  public void exit(NPC npc) {

  }

  @Override
  public boolean onMessage(NPC npc, Telegram telegram) {
    return false;
  }
}
