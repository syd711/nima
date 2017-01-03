package com.nima.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.nima.actors.Spine;
import com.nima.components.SpineComponent;

public class SpineRenderSystem extends AbstractIteratingSystem {
  private ComponentMapper<SpineComponent> spineMap = ComponentMapper.getFor(SpineComponent.class);

  private BatchTiledMapRenderer renderer;

  public SpineRenderSystem(BatchTiledMapRenderer renderer) {
    super(Family.all(SpineComponent.class).get());
    this.renderer = renderer;
  }

  public void process(Entity entity, float deltaTime) {
    Spine spine = (Spine) entity;
    //spine rendering
    spine.state.update(Gdx.graphics.getDeltaTime()); // Update the animation time.
    spine.state.apply(spine.skeleton); // Poses skeleton using current animations. This sets the bones' local SRT.
    spine.skeleton.updateWorldTransform();
    spine.skeletonRenderer.draw(renderer.getBatch(), spine.skeleton); // Draw the skeleton images.
  }
}