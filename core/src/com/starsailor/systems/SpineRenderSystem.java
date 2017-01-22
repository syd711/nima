package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.Spine;
import com.starsailor.components.BodyComponent;
import com.starsailor.components.PositionComponent;
import com.starsailor.components.SpineComponent;
import com.starsailor.util.GraphicsUtil;

public class SpineRenderSystem extends AbstractIteratingSystem {
  private BatchTiledMapRenderer renderer;

  public SpineRenderSystem(BatchTiledMapRenderer renderer) {
    super(Family.all(SpineComponent.class).get());
    this.renderer = renderer;
  }

  public void process(Entity entity, float deltaTime) {
    PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
    BodyComponent bodyComponent = entity.getComponent(BodyComponent.class);

    //TODO only render active spines
    Spine spine = (Spine) entity;
    //spine rendering
    spine.state.update(Gdx.graphics.getDeltaTime()); // Update the animation time.
    spine.state.apply(spine.skeleton); // Poses skeleton using current animations. This sets the bones' local SRT.
    spine.skeleton.updateWorldTransform();
    spine.skeletonRenderer.draw(renderer.getBatch(), spine.skeleton); // Draw the skeleton images.

    Vector2 position = bodyComponent.getWorldPosition();
    float bodyAngle = bodyComponent.body.getAngle();
    Vector2 targetVector = new Vector2();
    GraphicsUtil.angleToVector(targetVector, bodyAngle);
    positionComponent.setPosition(position);
    spine.skeleton.setPosition(positionComponent.x, positionComponent.y);
  }
}