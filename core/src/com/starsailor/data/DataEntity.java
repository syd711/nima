package com.starsailor.data;

public class DataEntity extends GameDataWithId<DataEntity> {

  public DataEntity(int id, DataEntity parent) {
    super(id, parent.getName());
  }

}
