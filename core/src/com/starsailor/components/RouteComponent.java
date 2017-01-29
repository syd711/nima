package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.Behaviours;
import com.starsailor.actors.NPC;
import com.starsailor.actors.RoutePoint;
import com.starsailor.data.ShipProfile;

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
  public Behaviours behaviour = Behaviours.PEACEFUL;

  public List<RoutePoint> routeCoordinates = new ArrayList<>();
  public RoutePoint spawnPoint;


  public void addGuard(ShipProfile ship, Vector2 centeredPosition, Behaviours behaviour) {
    guards.add(new Guard(ship, centeredPosition, behaviour));
  }

  public class Guard {
    public ShipProfile ship;
    public Vector2 centeredPosition;
    public Behaviours behaviour;

    public Guard(ShipProfile ship, Vector2 centeredPosition, Behaviours behaviour) {
      this.ship = ship;
      this.centeredPosition = centeredPosition;
      this.behaviour = behaviour;
    }
  }
}
