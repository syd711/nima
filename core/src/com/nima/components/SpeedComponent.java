package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * The current speed of an entity
 */
public class SpeedComponent implements Component {

  public float velocityUp = 0.05f;
  public float velocityDown = 0.05f;

  public float maxSpeed;
  public float currentSpeed;
  public float targetSpeed;

  public SpeedComponent(float maxSpeed) {
    this.maxSpeed = maxSpeed;
    this.currentSpeed = 0;
  }


  public boolean isAtTargetSpeed() {
    return currentSpeed == targetSpeed;
  }

  public void setTargetSpeedPercentage(float percentage) {
    targetSpeed = maxSpeed*percentage/100;
    targetSpeed = (float) (Math.round(targetSpeed * 100.0) / 100.0);
  }

  public void updateSpeed() {
    if(currentSpeed < targetSpeed) {
      currentSpeed+=velocityUp;
    }
    else if (currentSpeed > targetSpeed && currentSpeed > 0) {
      currentSpeed-=velocityDown;
    }
    currentSpeed = (float) (Math.round(currentSpeed * 100.0) / 100.0);
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

    setTargetSpeedPercentage(percentage);
  }
}
