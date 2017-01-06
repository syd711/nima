package com.nima.systems.states;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.Evade;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.math.Vector2;
import com.nima.actors.NPC;
import com.nima.actors.Player;
import com.nima.components.PositionComponent;
import com.nima.components.SteerableComponent;
import com.nima.systems.behaviours.FaceToPlayerBehaviour;

/**
 * The different states of an attack.
 */
public enum AttackState implements State<NPC> {

  SLEEP() {
    @Override
    public void enter(NPC npc) {
      SteerableComponent steerableComponent = npc.getComponent(SteerableComponent.class);
      steerableComponent.setBehavior(null);
      steerableComponent.setFaceBehaviour(null);
    }

    @Override
    public void update(NPC entity) {
      updateState(entity);
    }
  },
  PURSUE() {
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
      updateState(npc);
    }
  },
  FACE() {
    @Override
    public void enter(NPC npc) {
      SteerableComponent steerableComponent = npc.getComponent(SteerableComponent.class);
      FaceToPlayerBehaviour face = new FaceToPlayerBehaviour(npc);
      steerableComponent.setBehavior(null);
      steerableComponent.setFaceBehaviour(face);
    }

    @Override
    public void update(NPC npc) {
      updateState(npc);
    }
  },
  EVADE() {
    @Override
    public void enter(NPC npc) {
      SteerableComponent steerableComponent = npc.getComponent(SteerableComponent.class);
      Evade<Vector2> evade = new Evade<>(steerableComponent, Player.getInstance().getComponent(SteerableComponent.class));
      steerableComponent.setBehavior(evade);
    }

    @Override
    public void update(NPC entity) {
      updateState(entity);
    }
  };

  @Override
  public void enter(NPC entity) {

  }

  @Override
  public void exit(NPC entity) {

  }

  @Override
  public boolean onMessage(NPC entity, Telegram telegram) {
    return false;
  }

  // ------------------- Helper -----------------------------------
  private static void updateState(NPC npc) {
    AttackState newState = null;
    float distance = getDistanceToPlayer(npc);
    if(distance < 250) {
      newState = AttackState.EVADE;
    }
    else if(distance > 250 && distance < 350){
      newState = AttackState.FACE;
    }
    else {
      newState = AttackState.PURSUE;
    }

    if(npc.attackStateMachine.getCurrentState() != newState) {
      npc.attackStateMachine.changeState(newState);
    }
  }

  private static float getDistanceToPlayer(NPC npc) {
    PositionComponent npcPosition = npc.getComponent(PositionComponent.class);
    PositionComponent playerPosition = Player.getInstance().getComponent(PositionComponent.class);
    return npcPosition.getPosition().dst(playerPosition.getPosition());
  }
}
