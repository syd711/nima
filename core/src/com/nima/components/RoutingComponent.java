package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;
import com.nima.Game;
import com.nima.data.RoutePoint;
import com.nima.managers.EntityManager;
import com.nima.util.BodyGenerator;

import java.util.Iterator;
import java.util.List;

/**
 * Contains the path finding status
 */
public class RoutingComponent implements Component, Pool.Poolable {

  public RoutePoint target;
  public List<RoutePoint> targets;

  private SteerableComponent steerableComponent;
  private Body body;

  private Iterator<RoutePoint> iterator;

  @Override
  public void reset() {
    target = null;
    targets = null;
    iterator = null;
  }

  public RoutePoint nextTarget() {
    if(body != null) {
      Game.world.destroyBody(body);
    }

    //create iterator and move to spawn point
    if(iterator == null) {
      iterator = targets.iterator();
      while(iterator.hasNext()) {
        RoutePoint next = iterator.next();
        if(next.equals(target)) {
          break;
        }
      }
    }

    if(!iterator.hasNext()) {
      iterator = targets.iterator();
    }

    return iterator.next();
  }

  public SteerableComponent getSteeringComponent(RoutePoint point) {
    steerableComponent = EntityManager.getInstance().createComponent(SteerableComponent.class);
    body = BodyGenerator.generateRoutePointBody(point);
    body.setUserData(point);
    steerableComponent.init(body, point);
    return steerableComponent;
  }
}
