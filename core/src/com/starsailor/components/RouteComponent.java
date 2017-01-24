package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.starsailor.actors.NPC;
import com.starsailor.actors.RoutePoint;
import com.starsailor.data.ShipProfile;
import com.starsailor.render.converters.MapConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the path finding status
 * Component is not poolable since all are created once.
 */
public class RouteComponent implements Component {

  public String name;

  //the profile is needed here to respawn ships
  public ShipProfile shipProfile;
  public NPC npc;
  public String behaviour = MapConstants.BEHAVIOUR_PEACEFUL;

  public List<RoutePoint> routeCoordinates = new ArrayList<>();
  public RoutePoint spawnPoint;
}
