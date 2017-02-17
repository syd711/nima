package com.starsailor.util;

import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.Ship;

/**
 * Just for debugging purposes
 */
public class Debugger {

  public static void log(Ship ship) {
    StringBuilder builder = new StringBuilder("\n");
    builder.append("===============================================================================================\n");
    builder.append(ship.toString() + "\n");
    builder.append("-----------------------------------------------------------------------------------------------\n");
    SteeringBehavior<Vector2> behavior = ship.steerableComponent.getBehavior();
    builder.append("Behaviour: " + behavior + "\n");
    if(behavior instanceof BlendedSteering) {
      BlendedSteering blendedSteering = (BlendedSteering) behavior;
      int index = 0;
      while(true) {
        BlendedSteering.BehaviorAndWeight behaviorAndWeight = null;
        try {
          behaviorAndWeight = blendedSteering.get(index);
        } catch (Exception e) {
          break;
        }
        builder.append(" - " + behaviorAndWeight.getBehavior() + ": " + behaviorAndWeight.getWeight() + "\n");
        index++;
      }
    }

    builder.append(" - Max Linear Acceleration: " + ship.steerableComponent.getMaxLinearAcceleration() + "\n");
    builder.append(" - Max Linear Speed: " + ship.steerableComponent.getMaxLinearSpeed() + "\n");
    builder.append("===============================================================================================\n");

    System.out.println(builder.toString());
  }
}
