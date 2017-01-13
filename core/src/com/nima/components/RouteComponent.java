package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.nima.actors.NPC;
import com.nima.data.RoutePoint;
import com.nima.data.ShipProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the path finding status
 */
public class RouteComponent implements Component {

  public String name;
  public ShipProfile shipProfile;
  public NPC npc;

  public List<RoutePoint> routeCoordinates = new ArrayList<>();
  public RoutePoint spawnPoint;
}
