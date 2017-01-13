package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

import java.util.List;

/**
 * Contains the path finding status
 */
public class RoutingComponent implements Component, Pool.Poolable {

  public Vector2 target;
  public List<Vector2> targets;

  @Override
  public void reset() {
    target = null;
    targets = null;
  }
}
