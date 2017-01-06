package com.nima.systems.behaviours;

import com.badlogic.gdx.math.Vector2;
import com.nima.actors.NPC;
import com.nima.actors.Player;
import com.nima.components.BodyComponent;

/**
 * Custom FACE behaviour.
 * <p>
 * We pretend it to be a real SteeringBehaviour to use the update method.
 */
public class FaceToPlayerBehaviour implements FaceBehaviour {
  private final BodyComponent bodyComponent;
  private final BodyComponent targetBodyComponent;

  public FaceToPlayerBehaviour(NPC npc) {
    bodyComponent = npc.getComponent(BodyComponent.class);
    targetBodyComponent = Player.getInstance().getComponent(BodyComponent.class);
  }

  public void update() {
    //in Step() function
    float bodyAngle = bodyComponent.body.getAngle();
    Vector2 toTarget = targetBodyComponent.body.getPosition().sub(bodyComponent.body.getPosition());
    float desiredAngle = (float) Math.atan2(-toTarget.x, toTarget.y);

    float totalRotation = desiredAngle - bodyAngle;
    float change = (float) (1 * Math.toDegrees(1)); //allow 1 degree rotation per time step
    float newAngle = bodyAngle + Math.min(change, Math.max(-change, totalRotation));

//    System.out.println(Math.toDegrees(newAngle));
    bodyComponent.body.setTransform(bodyComponent.body.getPosition(), newAngle);
  }
}
