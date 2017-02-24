package com.starsailor.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads json
 */
abstract public class JsonDataFactory {

  protected static <T> Map<String, T> createDataEntities(String filename, Class<T> entity) {
    Map<String, T> ts = new HashMap<String, T>();
    FileHandle internal = Gdx.files.internal(filename);
    FileHandle[] list = internal.list();
    for(FileHandle fileHandle : list) {
      T dataEntity = loadDataEntity(fileHandle, entity);
      ts.put(toName(fileHandle), dataEntity);
    }
    return ts;
  }

  private static String toName(FileHandle fileHandle) {
    String name = fileHandle.file().getName();
    return name.substring(0, name.lastIndexOf("."));
  }

  public static <T> T loadDataEntity(FileHandle fileHandle, Class<T> entity) {
    T t = loadDataEntity(fileHandle.file(), entity);
    return t;
  }

  public static <T> T saveDataEntity(File file, Object entity) {
    return saveDataEntity(file, entity, null, null);
  }

  public static <T> T saveDataEntity(File file, Object entity, Class clazz, JsonSerializer serializer) {
    try {
      GsonBuilder gsonBuilder = new GsonBuilder();
      if(serializer != null) {
        gsonBuilder.registerTypeAdapter(clazz, serializer);
      }
      Gson gson = gsonBuilder.setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
      String json = gson.toJson(entity);
      FileWriter writer = new FileWriter(file);
      writer.write(json);
      writer.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static <T> T loadDataEntity(File file, Class<T> entity) {
    try {
      Gson gson = new Gson();
      FileReader fileReader = new FileReader(file);
      JsonReader reader = new JsonReader(fileReader);
      return gson.fromJson(reader, entity);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
