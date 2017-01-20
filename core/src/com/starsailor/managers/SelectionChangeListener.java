package com.starsailor.managers;

import com.starsailor.actors.GameEntity;

/**
 * Interface to be implemented for components listening on change events.
 */
public interface SelectionChangeListener {

  void selectionChanged(GameEntity oldSelection, GameEntity newSelection);
}
