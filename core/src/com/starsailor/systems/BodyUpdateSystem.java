package com.starsailor.systems;

import com.badlogic.ashley.core.Family;
import com.starsailor.actors.GameEntity;
import com.starsailor.actors.Ship;
import com.starsailor.components.ShipBodyComponent;

public class BodyUpdateSystem extends PauseableIteratingSystem {
  public BodyUpdateSystem() {
    super(Family.all(ShipBodyComponent.class).get());
  }

  public void process(GameEntity entity, float deltaTime) {
    Ship ship = (Ship) entity;
    ship.shipBodyComponent.updateBody();
  }
}