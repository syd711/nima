package com.starsailor.util;

import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.GameEntity;
import com.starsailor.actors.Ship;
import com.starsailor.actors.route.Route;
import com.starsailor.managers.EntityManager;
import com.starsailor.ui.UIManager;

import java.util.List;

/**
 * Just for debugging purposes
 */
public class Debugger {

  public static void log() {
    StringBuilder builder = new StringBuilder("\n");

    List<GameEntity> entities = EntityManager.getInstance().getEntities(GameEntity.class);
    for(GameEntity entity : entities) {
      if(entity instanceof Ship) {
        logShip(builder, (Ship) entity);
      }
      else if(entity instanceof Route) {
        logRoute(builder, (Route) entity);
      }
    }

    logUI(builder);

    System.out.println(builder.toString());
  }

  private static void logUI(StringBuilder builder) {
    UIManager.getInstance().log(builder);
  }

  private static void logRoute(StringBuilder builder, Route route) {
    builder.append("= Route =======================================================================================\n");
    builder.append(route.getName() + "\n");
    builder.append("-----------------------------------------------------------------------------------------------\n");
  }

  private static void logShip(StringBuilder builder, Ship ship) {
    builder.append("= Ship ========================================================================================\n");
    builder.append(ship.toString() + "\n");
    builder.append("-----------------------------------------------------------------------------------------------\n");
    builder.append("State: " + ship.statefulComponent.stateMachine.getCurrentState() + "\n");
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
    builder.append("-----------------------------------------------------------------------------------------------\n");
    builder.append("\n");
  }
}
