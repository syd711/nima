package com.starsailor.savegame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Pojo for storing the game data
 */
public class SaveGameItem {

  private Map<String,Object> status = new HashMap<>();

  private List<SaveGameItem> items = new ArrayList<>();

  public void addSaveGameItem(SaveGameItem item) {
    items.add(item);
  }

  public String getString(String key) {
    return (String) status.get(key);
  }

  public int getInt(String key) {
    return ((Double)status.get(key)).intValue();
  }

  public float getFloat(String key) {
    return (float) status.get(key);
  }

  public List<SaveGameItem> getItems() {
    return items;
  }

  public void store(String key, Object value) {
    status.put(key, value);
  }
}
