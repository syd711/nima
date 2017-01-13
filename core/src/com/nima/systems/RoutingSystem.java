package com.nima.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.nima.components.RoutingComponent;

public class RoutingSystem extends AbstractIteratingSystem {
  public RoutingSystem() {
    super(Family.all(RoutingComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {

  }
}