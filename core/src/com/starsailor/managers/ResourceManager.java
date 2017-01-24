package com.starsailor.managers;

import com.badlogic.gdx.files.FileHandle;

/**
 * Utility superclass for resource based manager singletons.
 */
abstract public class ResourceManager {

  protected String getEnumName(FileHandle fileHandle) {
    return fileHandle.name().toUpperCase().substring(0, fileHandle.name().lastIndexOf("."));
  }
}
