package com.nima.data;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A ship route used for path finding
 */
public class RouteProfile {
  public String name;
  public List<String> stations = new ArrayList<>();
  public boolean circulating = false;

  public Map<String,Vector2> coordinates = new HashMap<>();

  public RouteProfile(String name) {
    this.name = name;
  }
}
