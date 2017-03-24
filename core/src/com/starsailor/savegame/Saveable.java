package com.starsailor.savegame;

/**
 * Interface to be implemented if the given class stores data to be save and restored.
 */
public interface Saveable {

  void save(SaveGameItem saveGame);
}
