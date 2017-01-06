package com.nima.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.nima.components.ShootingComponent;

/**
 * Used during fighting
 */
public class ShootingSystem extends IteratingSystem {
  private ComponentMapper<ShootingComponent> shootingMap = ComponentMapper.getFor(ShootingComponent.class);

  public ShootingSystem() {
    super(Family.all(ShootingComponent.class).get());
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {

  }
}
