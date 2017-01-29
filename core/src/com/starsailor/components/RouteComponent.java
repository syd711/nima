package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.starsailor.actors.Behaviours;
import com.starsailor.actors.RoutePoint;
import com.starsailor.data.ShipProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the path finding status
 * Component is not poolable since all are created once.
 */
public class RouteComponent implements Component {

  public List<RoutePoint> routeCoordinates = new ArrayList<>();
  public RoutePoint spawnPoint;

}
