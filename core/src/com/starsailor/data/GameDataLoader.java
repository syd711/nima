package com.starsailor.data;

import java.io.File;

/**
 * Used to load the json data model
 */
public class GameDataLoader {

  private File jsonFile;

  public GameDataLoader(File jsonFile) {
    this.jsonFile = jsonFile;
  }

  public <T> T load(Class<T> entity) {
    return JsonDataFactory.loadDataEntity(jsonFile, entity);
  }
}
