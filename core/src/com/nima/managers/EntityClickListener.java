package com.nima.managers;

import com.badlogic.ashley.core.Entity;

/**
 * Listener to react on entity clicks
 */
public interface EntityClickListener {

  void entityClicked(Entity entity);
}
