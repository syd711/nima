package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.Spine;
import com.starsailor.components.BodyComponent;
import com.starsailor.components.PositionComponent;
import com.starsailor.components.SpineComponent;

public class SpineRenderSystem extends RenderingSystem {

  public SpineRenderSystem(Batch batch) {
    super(batch, Family.all(SpineComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    //TODO only render active spines
    Spine spine = (Spine) entity;

    PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
    BodyComponent bodyComponent = entity.getComponent(BodyComponent.class);

    //apply box2d world to spine world
    Vector2 position = bodyComponent.getWorldPosition();
    float bodyAngle = bodyComponent.body.getAngle();

    //TODO fix spine angle and position here!
    positionComponent.setPosition(position);
    spine.skeleton.setPosition(positionComponent.x, positionComponent.y);
    spine.skeleton.getRootBone().setRootRotation((float) Math.toDegrees(bodyAngle));

    //apply the rendering to the spine engine
    spine.state.apply(spine.skeleton); // Poses skeleton using current animations. This sets the bones' local SRT.
    spine.skeleton.updateWorldTransform();
    spine.skeletonRenderer.draw(batch, spine.skeleton); // Draw the skeleton images.
  }
}