package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.GameEntity;
import com.starsailor.components.*;

import java.util.List;

public class SpineRenderSystem extends RenderingSystem {

  //TODO add marker component
  public SpineRenderSystem(Batch batch) {
    super(batch, Family.all(BodyComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
    BodyComponent bodyComponent = entity.getComponent(BodyComponent.class);

    List<SpineComponent> spineComponents = ((GameEntity)entity).getComponents(SpineComponent.class);
    for(SpineComponent spineComponent : spineComponents) {
      renderSpine(positionComponent, bodyComponent, spineComponent);
    }
  }

  private void renderSpine(PositionComponent positionComponent, BodyComponent bodyComponent, SpineComponent spineComponent) {
    //apply box2d world to spine world
    Vector2 position = bodyComponent.getWorldPosition();
    float bodyAngle = bodyComponent.body.getAngle();

    positionComponent.setPosition(position);
    spineComponent.getSkeleton().setPosition(positionComponent.x, positionComponent.y);
    spineComponent.getSkeleton().getRootBone().setRootRotation((float) Math.toDegrees(bodyAngle));

    //apply the rendering to the spine engine
    spineComponent.getAnimationState().apply(spineComponent.getSkeleton()); // Poses skeleton using current animations. This sets the bones' local SRT.
    spineComponent.getSkeleton().updateWorldTransform();
    spineComponent.getSkeletonRenderer().draw(batch, spineComponent.getSkeleton()); // Draw the skeleton images.
  }
}