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

  public List<SaveGameItem> getItems() {
    return items;
  }

  public void store(String key, Object value) {
    status.put(key, value);
  }

}
