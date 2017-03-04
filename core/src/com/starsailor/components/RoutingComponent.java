package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.starsailor.actors.RoutePoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.starsailor.util.Settings.MPP;

/**
 * Contains the path finding status
 */
public class RoutingComponent implements Component, Pool.Poolable {
  private List<RoutePoint> targets;

  @Override
  public void reset() {
    targets = null;
  }

  public Array<Vector2> getWayPoints(Vector2 origin) {
    List<Vector2> result = new ArrayList<>();
    int indexOfClosedPoint = findClosestIndex(targets, origin);
    int shift = targets.size() - indexOfClosedPoint;
    rotate(targets, shift);

    for(RoutePoint point : targets) {
      result.add(new Vector2(point.getPosition()).scl(MPP));
    }
    return new Array<>(result.toArray(new Vector2[result.size()]));
  }

  public void setTargets(List<RoutePoint> targets) {
    this.targets = targets;
    Collections.sort(targets, new Comparator<RoutePoint>() {
      @Override
      public int compare(RoutePoint o1, RoutePoint o2) {
        return o1.getIndex() - o2.getIndex();
      }
    });
  }

  private int findClosestIndex(List<RoutePoint> result, Vector2 origin) {
    RoutePoint closestPoint = null;
    for(RoutePoint point : result) {
      if(closestPoint == null || point.getPosition().dst(origin) < closestPoint.getPosition().dst(origin)) {
        closestPoint = point;
      }
    }

    return result.indexOf(closestPoint);
  }

  private static <T> List<T> rotate(List<T> aL, int shift) {
    if(aL.size() == 0)
      return aL;

    T element = null;
    for(int i = 0; i < shift; i++) {
      // remove last element, add it to front of the ArrayList
      element = aL.remove(aL.size() - 1);
      aL.add(0, element);
    }

    return aL;
  }
}
