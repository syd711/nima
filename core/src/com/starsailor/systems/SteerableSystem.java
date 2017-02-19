package com.starsailor.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Family;
import com.starsailor.actors.GameEntity;
import com.starsailor.components.SteerableComponent;

public class SteerableSystem extends PauseableIteratingSystem {
  private ComponentMapper<SteerableComponent> steerableMap = ComponentMapper.getFor(SteerableComponent.class);

  public SteerableSystem() {
    super(Family.all(SteerableComponent.class).get());
  }

  public void process(GameEntity entity, float deltaTime) {
    if(!entity.isMarkedForDestroy()) {
      SteerableComponent steerableComponent = steerableMap.get(entity);
      steerableComponent.update(deltaTime);
    }
  }
}