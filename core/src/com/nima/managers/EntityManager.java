package com.nima.managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;

/**
 * Created by Matthias on 18.12.2016.
 */
public class EntityManager {
  private final Engine engine;

  public EntityManager(Engine engine) {
    this.engine = engine;

    PlayerCollisionManager collisionSystem = new PlayerCollisionManager(engine);
    engine.addSystem(collisionSystem);
  }


  public void update() {
    engine.update(Gdx.graphics.getDeltaTime());
  }
}
