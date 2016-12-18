package com.nima.entities;

import com.badlogic.gdx.maps.MapObject;
import com.nima.render.ActorBasedTiledMultiMapRenderer;
import com.nima.render.CollisionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract super class used for classes that render on the layer that is responsible rendering
 * actors moving over this layer.
 */
abstract public class Actor extends GameEntity {

  public ActorBasedTiledMultiMapRenderer renderer;

  private List<CollisionListener> collisionListeners = new ArrayList();

  public Actor(ActorBasedTiledMultiMapRenderer renderer) {
    this.renderer = renderer;
  }

  public void addCollisionListener(CollisionListener listener) {
    this.collisionListeners.add(listener);
  }

  public void doRender() {
    render();

  }

  abstract public void render();

  abstract public float getX();

  abstract public float getY();

  abstract public void setPosition(float x, float y);

  abstract public boolean translate(int x, int y);

  abstract public boolean intersects(MapObject mapObject);

  abstract public boolean moveBy(int x, int y);
}
