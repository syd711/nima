package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Iterating system that renders
 */
abstract public class RenderingSystem extends IteratingSystem {

  protected SpriteBatch batch;

  public RenderingSystem(Batch batch, Family family) {
    super(family);
    this.batch = (SpriteBatch) batch;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    batch.begin();
    process(entity, deltaTime);
    batch.end();
  }

  abstract protected void process(Entity entity, float deltaTime);
}
