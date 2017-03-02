package com.starsailor.model.items;

import com.starsailor.model.GameDataWithId;

/**
 * Created by Matthias on 02.03.2017.
 */
abstract public class MapItem<T>  extends GameDataWithId<T>  {

  public MapItem(int id, String name) {
    super(id, name);
  }
}
