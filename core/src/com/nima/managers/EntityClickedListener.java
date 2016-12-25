package com.nima.managers;

import com.badlogic.ashley.core.Entity;

/**
 * Listener to react on entity clicks
 */
public interface EntityClickedListener {

  void entityClicked(Entity entity);
  void entityDoubleClicked(Entity entity);
}
