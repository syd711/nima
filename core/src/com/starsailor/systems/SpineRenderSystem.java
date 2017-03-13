package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.components.*;

public class SpineRenderSystem extends RenderingSystem {

  //TODO add marker component
  public SpineRenderSystem(Batch batch) {
    super(batch, Family.all(BodyComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
    BodyComponent bodyComponent = entity.getComponent(BodyComponent.class);

    //render ship
    SpineShipComponent spineShipComponent = entity.getComponent(SpineShipComponent.class);
    if(spineShipComponent != null) {
      renderSpine(positionComponent, bodyComponent, spineShipComponent);
    }

    //render shield
    SpineShieldComponent spineShieldComponent = entity.getComponent(SpineShieldComponent.class);
    if(spineShieldComponent != null) {
      renderSpine(positionComponent, bodyComponent, spineShieldComponent);
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