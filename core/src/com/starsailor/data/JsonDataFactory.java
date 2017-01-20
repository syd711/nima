package com.starsailor.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads json
 */
abstract class JsonDataFactory {

  protected static <T> Map<String, T> createDataEntities(String filename, Class<T> entity) {
    Map<String, T> ts = new HashMap<String, T>();
    FileHandle internal = Gdx.files.internal(filename);
    FileHandle[] list = internal.list();
    for(FileHandle fileHandle : list) {
      T dataEntity = createDataEntity(fileHandle, entity);
      ts.put(toName(fileHandle), dataEntity);
    }
    return ts;
  }

  private static String toName(FileHandle fileHandle) {
    String name = fileHandle.file().getName();
    return name.substring(0, name.lastIndexOf("."));
  }

  private static <T> T createDataEntity(FileHandle fileHandle, Class<T> entity) {
    try {
      Gson gson = new Gson();
      FileReader fileReader = new FileReader(fileHandle.file());
      JsonReader reader = new JsonReader(fileReader);
      return gson.fromJson(reader, entity);
    } catch (Exception e) {
      Gdx.app.error(JsonDataFactory.class.getName(), "Error loading json file", e);
    }
    return null;
  }
}
