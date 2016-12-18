package com.nima.managers;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.nima.components.PlayerComponent;

/**
 * Created by Matthias on 18.12.2016.
 */
public class PlayerCollisionManager extends IteratingSystem {

  private ComponentMapper<PlayerComponent> componentMapper = ComponentMapper.getFor(PlayerComponent.class);

  public PlayerCollisionManager(Engine engine) {
    super(Family.all(PlayerComponent.class).get());
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    PlayerComponent player = componentMapper.get(entity);
  }
}
