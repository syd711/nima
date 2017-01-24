package com.starsailor.actors;

import com.starsailor.components.SelectionComponent;

/**
 * Interface to be implemented by selectable entities.
 */
public interface Selectable {

  SelectionComponent getSelectionComponent();

  void setSelected(boolean b);
}
