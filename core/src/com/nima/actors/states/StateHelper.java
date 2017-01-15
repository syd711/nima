package com.nima.actors.states;

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
 * Returns the state for the given behaviour
 */
public class StateHelper {

  /**
   * Face the player without moving.
   */
  public static void facePlayer(NPC npc) {
    SteerableComponent steerableComponent = npc.getComponent(SteerableComponent.class);
    FaceToPlayerBehaviour face = new FaceToPlayerBehaviour(npc);
    steerableComponent.setBehavior(null);
    steerableComponent.setFaceBehaviour(face);
  }

  /**
   * Evading from the player.
   */
  public static void evadePlayer(NPC npc) {
    SteerableComponent steerableComponent = npc.getComponent(SteerableComponent.class);
    Evade<Vector2> evade = new Evade<>(steerableComponent, Player.getInstance().getComponent(SteerableComponent.class));
    steerableComponent.setBehavior(evade);
  }

  /**
   * Let the give npc follow its route.
   */
  public static void route(NPC npc) {
    SteerableComponent sourceSteering = npc.getComponent(SteerableComponent.class);
    RoutingComponent routingComponent = npc.getComponent(RoutingComponent.class);

    RoutePoint point = routingComponent.nextTarget();
    SteerableComponent targetSteering = routingComponent.getSteeringComponent(point);

    Arrive<Vector2> behaviour = new Arrive<>(sourceSteering, targetSteering);
    behaviour.setArrivalTolerance(0.10f);
    behaviour.setDecelerationRadius(1f);
    sourceSteering.setBehavior(behaviour);
    sourceSteering.setFaceBehaviour(null);
  }

  /**
   * Face the player and pursues him
   */
  public static void pursuePlayer(NPC npc) {
    SteerableComponent steerableComponent = npc.getComponent(SteerableComponent.class);
    Pursue<Vector2> behaviour = new Pursue<>(steerableComponent, Player.getInstance().getComponent(SteerableComponent.class));
    behaviour.setMaxPredictionTime(0.7f);
    steerableComponent.setBehavior(behaviour);
    steerableComponent.setFaceBehaviour(new FaceToPlayerBehaviour(npc));
  }

  /**
   * Returns the distance to the player for the give npc
   */
  public static float getDistanceToPlayer(NPC npc) {
    PositionComponent npcPosition = npc.getComponent(PositionComponent.class);
    PositionComponent playerPosition = Player.getInstance().getComponent(PositionComponent.class);
    return npcPosition.getPosition().dst(playerPosition.getPosition());
  }
}
