package com.nima.systems.behaviours;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.behaviors.Face;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.nima.actors.NPC;
import com.nima.actors.Player;
import com.nima.components.BodyComponent;

/**
 * Custom FACE behaviour.
 *
 * We pretend it to be a real SteeringBehaviour to use the update method.
 */
public class FaceBehaviour<T extends Vector<T>> extends Face<Vector2> {
  private final BodyComponent bodyComponent;
  private final BodyComponent targetBodyComponent;

  private SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(Vector2.Zero);

  public FaceBehaviour(Steerable<Vector2> owner, Location<Vector2> target, NPC npc, Player player) {
    super(owner, target);
    bodyComponent = npc.getComponent(BodyComponent.class);
    targetBodyComponent = player.getComponent(BodyComponent.class);
  }

  public void update() {
    //in Step() function
    float bodyAngle = bodyComponent.body.getAngle();
    Vector2 toTarget = targetBodyComponent.body.getPosition().sub(bodyComponent.body.getPosition());
    float desiredAngle = (float) Math.atan2(-toTarget.x, toTarget.y);

    float totalRotation = desiredAngle - bodyAngle;
    float change = (float) (1 * Math.toDegrees(1)); //allow 1 degree rotation per time step
    float newAngle = bodyAngle + Math.min(change, Math.max(-change, totalRotation));
    bodyComponent.body.setTransform(bodyComponent.body.getPosition(), newAngle);
  }

  @Override
  protected SteeringAcceleration<Vector2> calculateRealSteering(SteeringAcceleration<Vector2> steering) {
    update();
    return steeringOutput;
  }
}
