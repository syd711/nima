package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Polygon;
import com.nima.Game;
import com.nima.actors.Spine;
import com.nima.render.MapConstants;
import com.nima.render.TiledMultiMapRenderer;
import com.nima.util.PolygonUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Component responsible for handling the collision of entities
 */
public class CollisionComponent implements Component {
  public List<Object> collisionComponents = new ArrayList<>();
  private List<CollisionComponent> collidingObjects = new ArrayList<>();

  public CollisionComponent(Spine spine) {
    PolygonUtil.createSpineBody(Game.world, spine);
  }

  /**
   * Constructor for tiled map objects.
   * The map objects have a fix position, so we can immediately calculate
   * the polygon for it.
   *
   * @param object the map object to create the polygon for
   */
  public CollisionComponent(MapObject object) {
    Object collisionObject = object.getProperties().get(MapConstants.PROPERTY_COLLISION_COMPONENT);
    collisionComponents.add(collisionObject);
    TiledMultiMapRenderer.debugRenderer.render(collisionObject);
  }

  /**
   * Checks the collision of an entity with the owner
   * of the Dimension Component.
   * We know that his method is always called from a spine or sprite, so
   * the source is always a polygon!
   */
  public boolean collidesWith(Entity entity, CollisionComponent collisionComponent) {
    if(!collisionComponent.collisionComponents.isEmpty()) {
      return PolygonUtil.checkForCollision(collisionComponent.collisionComponents, this.collisionComponents);
    }
    return false;
  }

  /**
   * Checks if the given click point is inside another entity.
   * The clickpoint is a 10x10 rectange.
   */
  public boolean collidesWith(Polygon clickPoint) {
    return PolygonUtil.checkForCollision(Arrays.asList(clickPoint), this.collisionComponents);
  }

  // -------------- Mandatory for storing the collision state ---------------------------

  public boolean isColliding(CollisionComponent collisionComponent) {
    return collidingObjects.contains(collisionComponent);
  }

  public void addCollision(CollisionComponent collisionComponent) {
    this.collidingObjects.add(collisionComponent);
  }

  public void removeCollision(CollisionComponent collisionComponent) {
    this.collidingObjects.remove(collisionComponent);
  }
}
