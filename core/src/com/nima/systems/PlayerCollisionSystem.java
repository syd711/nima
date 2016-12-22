package com.nima.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.nima.actors.Player;
import com.nima.components.CollisionComponent;
import com.nima.components.MapObjectComponent;
import com.nima.managers.EntityManager;

import java.util.logging.Logger;

public class PlayerCollisionSystem extends IteratingSystem {
  private static final Logger LOG = Logger.getLogger(MapObjectComponent.class.getName());

  private ComponentMapper<CollisionComponent> collisionsMap = ComponentMapper.getFor(CollisionComponent.class);

  public PlayerCollisionSystem() {
    super(Family.all(CollisionComponent.class).get());
  }

  public void processEntity(Entity entity, float deltaTime) {
    //check all entities except the player itself
    Player player = EntityManager.getInstance().getPlayer();
    CollisionComponent playerCollision = player.getCollisionComponent();
    if(entity == player.getEntity()) {
      //enforces the polygon to update depending on the position
      playerCollision.updateBody();
      return;
    }
    CollisionComponent collisionComponent = collisionsMap.get(entity);
    if(playerCollision.collidesWith(entity, collisionComponent)) {
      LOG.info("Player has hit " + collisionComponent);
    }
  }
}