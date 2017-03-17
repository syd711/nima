package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.GameEntity;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.components.*;

import java.util.List;

public class SpineRenderSystem extends RenderingSystem {

  public SpineRenderSystem(Batch batch) {
    super(batch, Family.all(SpineMarkerComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
    BodyComponent bodyComponent = entity.getComponent(ShipBodyComponent.class);

    if(entity instanceof Bullet) {
      bodyComponent = entity.getComponent(BodyComponent.class);
    }

    List<SpineComponent> spineComponents = ((GameEntity)entity).getComponents(SpineComponent.class);
    for(SpineComponent spineComponent : spineComponents) {
      if(spineComponent.isEnabled()) {
        renderSpine(positionComponent, bodyComponent, spineComponent);
      }
    }
  }

  private void renderSpine(PositionComponent positionComponent, BodyComponent bodyComponent, SpineComponent spineComponent) {
    //apply box2d world to spine world
    Vector2 position = bodyComponent.getWorldPosition();
    float bodyAngle = bodyComponent.body.getAngle();

    positionComponent.setPosition(position);
    spineComponent.setPosition(positionComponent.x, positionComponent.y);
    spineComponent.setRotation((float) Math.toDegrees(bodyAngle));

    spineComponent.render(batch);
  }
}