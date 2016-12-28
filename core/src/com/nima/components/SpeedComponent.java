package com.nima.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * The current speed of an entity
 */
public class SpeedComponent extends DelimitingComponent {

  public SpeedComponent(float maxSpeed) {
    super(0, 0, maxSpeed, 0, 0);
  }

  public void calculateTargetSpeed(Vector2 point1, Vector2 point2) {
    float distance = point1.dst(point2);
    float percentage = 0;
    if(distance > 0) {
      float maxDistance = Gdx.graphics.getHeight() / 2;
      if(distance > maxDistance) {
        distance = maxDistance;
      }

      percentage = distance * 100 / maxDistance;
    }

    //lets fix the lower bound
    if(percentage < 15) {
      percentage = 0;
    }

    //lets fix the upper bound
    if(percentage > 70) {
      percentage = 100;
    }

    setTargetPercentage(percentage);
  }
}
