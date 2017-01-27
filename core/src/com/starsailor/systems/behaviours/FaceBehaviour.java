package com.starsailor.systems.behaviours;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * Custom face behaviours
 */
public interface FaceBehaviour {

  void update();

  boolean isValid();

  void setTarget(Body body);
}
