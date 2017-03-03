package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.starsailor.actors.RoutePoint;

import java.util.ArrayList;
import java.util.List;

import static com.starsailor.util.Settings.MPP;

/**
 * Contains the path finding status
 */
public class RoutingComponent implements Component, Pool.Poolable {

  public List<RoutePoint> targets;

  private Array<Vector2> wayPoints;

  @Override
  public void reset() {
    targets = null;
  }

  public Array<Vector2> getWayPoints() {
    if(wayPoints == null) {
      //TODO sort by index!
      List<Vector2> wp = new ArrayList<>();
      for(RoutePoint routePoint : targets) {
        wp.add(new Vector2(routePoint.getPosition()).scl(MPP));
      }
      this.wayPoints = new Array<>(wp.toArray(new Vector2[wp.size()]));
    }
    return wayPoints;
  }
}
