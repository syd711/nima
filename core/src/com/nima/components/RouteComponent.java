package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.nima.data.ShipProfile;
import com.nima.util.GraphicsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the path finding status
 */
public class RouteComponent implements Component {

  public String name;
  public ShipProfile shipProfile;
  public List<Vector2> routeCoordinates = new ArrayList<>();
  public Vector2 spawnPoint;

  public long lastSpawnTime;
}
