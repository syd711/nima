package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.starsailor.components.AnimationComponent;

public class AnimationRenderSystem extends RenderingSystem {

  public AnimationRenderSystem(Batch batch) {
    super(batch, Family.all(AnimationComponent.class).get());
  }

  @Override
  public void process(Entity entity, float deltaTime) {
    AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
    animationComponent.draw(batch, deltaTime);
  }
}