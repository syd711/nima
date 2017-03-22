package com.starsailor.systems;

import com.badlogic.ashley.core.Family;
import com.starsailor.actors.GameEntity;
import com.starsailor.actors.Ship;
import com.starsailor.components.BodyShipComponent;

public class BodyUpdateSystem extends PauseableIteratingSystem {
  public BodyUpdateSystem() {
    super(Family.all(BodyShipComponent.class).get());
  }

  public void process(GameEntity entity, float deltaTime) {
    Ship ship = (Ship) entity;
    ship.bodyShipComponent.updateBody();
  }
}