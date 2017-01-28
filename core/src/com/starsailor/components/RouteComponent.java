package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
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
  public List<Guard> guards = new ArrayList<>();
  public String behaviour = MapConstants.BEHAVIOUR_PEACEFUL;

  public List<RoutePoint> routeCoordinates = new ArrayList<>();
  public RoutePoint spawnPoint;


  public void addGuard(ShipProfile ship, Vector2 centeredPosition, String behaviour) {
    guards.add(new Guard(ship, centeredPosition, behaviour));
  }

  class Guard {
    private ShipProfile ship;
    private Vector2 centeredPosition;
    private String behaviour;

    public Guard(ShipProfile ship, Vector2 centeredPosition, String behaviour) {
      this.ship = ship;
      this.centeredPosition = centeredPosition;
      this.behaviour = behaviour;
    }
  }
}
