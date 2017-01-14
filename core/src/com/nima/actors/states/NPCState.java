package com.nima.actors.states;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.Evade;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.math.Vector2;
import com.nima.actors.NPC;
import com.nima.actors.Player;
import com.nima.components.PositionComponent;
import com.nima.components.RoutingComponent;
import com.nima.components.SteerableComponent;
import com.nima.data.RoutePoint;
import com.nima.systems.behaviours.FaceToPlayerBehaviour;

/**
 * The different states of an attack.
 */
public enum NPCState implements State<NPC> {

  IDLE() {
    @Override
    public void update(NPC npc) {

    }
  },
  ROUTE() {
    @Override
    public void enter(NPC npc) {
      SteerableComponent sourceSteering = npc.getComponent(SteerableComponent.class);
      RoutingComponent routingComponent = npc.getComponent(RoutingComponent.class);

      RoutePoint point = routingComponent.nextTarget();
      System.out.println(npc + " is pursueing " + point);
      SteerableComponent targetSteering = routingComponent.getSteeringComponent(point);

      Arrive<Vector2> behaviour = new Arrive<>(sourceSteering, targetSteering);
      behaviour.setArrivalTolerance(0.10f);
      behaviour.setDecelerationRadius(1f);
      sourceSteering.setBehavior(behaviour);
      sourceSteering.setFaceBehaviour(null);
    }

    @Override
    public void update(NPC entity) {
      //check route state
    }
  },
  ROUTE_POINT_ARRIVED() {
    @Override
    public void update(NPC npc) {
      State previousState = npc.getStateMachine().getPreviousState();
      npc.getStateMachine().changeState(previousState);
    }
  },
  ROUTE_AGGRESSIVE() {
    @Override
    public void enter(NPC npc) {
      SteerableComponent sourceSteering = npc.getComponent(SteerableComponent.class);
      RoutingComponent routingComponent = npc.getComponent(RoutingComponent.class);

      RoutePoint point = routingComponent.nextTarget();
      System.out.println(npc + " is pursueing " + point);
      SteerableComponent targetSteering = routingComponent.getSteeringComponent(point);

      Arrive<Vector2> behaviour = new Arrive<>(sourceSteering, targetSteering);
      behaviour.setArrivalTolerance(0.10f);
      behaviour.setDecelerationRadius(1f);
      sourceSteering.setBehavior(behaviour);
      sourceSteering.setFaceBehaviour(null);
    }

    @Override
    public void update(NPC npc) {
      if(shouldAttack(npc)) {
        npc.getStateMachine().changeState(PURSUE);
      }
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
    NPCState newState = null;
    float distance = getDistanceToPlayer(npc);
    if(distance < 250) {
      newState = NPCState.EVADE;
    }
    else if(distance > 250 && distance < 350){
      newState = NPCState.FACE;
    }
    else if(distance > 350 && distance < 500) {
      newState = NPCState.PURSUE;
    }
    else {
      newState = NPCState.ROUTE_AGGRESSIVE;
    }

    if(npc.getStateMachine().getCurrentState() != newState) {
      npc.getStateMachine().changeState(newState);
    }
  }

  private static boolean shouldAttack(NPC npc) {
    float distance = getDistanceToPlayer(npc);
    return distance < 200;
  }

  private static float getDistanceToPlayer(NPC npc) {
    PositionComponent npcPosition = npc.getComponent(PositionComponent.class);
    PositionComponent playerPosition = Player.getInstance().getComponent(PositionComponent.class);
    return npcPosition.getPosition().dst(playerPosition.getPosition());
  }
}
