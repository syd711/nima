package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.nima.actors.NPC;
import com.nima.data.RoutePoint;
import com.nima.data.ShipProfile;
import com.nima.render.converters.MapConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the path finding status
 * Component is not poolable since all are created once.
 */
public class RouteComponent implements Component {

  public String name;
  public ShipProfile shipProfile;
  public NPC npc;
  public String behaviour = MapConstants.BEHAVIOUR_PEACEFUL;

  public List<RoutePoint> routeCoordinates = new ArrayList<>();
  public RoutePoint spawnPoint;
}
