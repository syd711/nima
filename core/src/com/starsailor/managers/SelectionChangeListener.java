package com.starsailor.managers;

import com.starsailor.actors.Selectable;

/**
 * Interface to be implemented for components listening on change events.
 */
public interface SelectionChangeListener {

  void selectionChanged(Selectable oldSelection, Selectable newSelection);
}
