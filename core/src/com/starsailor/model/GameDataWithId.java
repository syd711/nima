package com.starsailor.model;

import com.google.gson.annotations.Expose;

/**
 * Superclass for all game data entities
 */
public class GameDataWithId<T> extends GameData<T> {

  @Expose
  private String name;

  @Expose
  private int id;

  public GameDataWithId(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }
}
