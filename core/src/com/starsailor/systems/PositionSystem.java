package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Spine;
import com.starsailor.components.BodyComponent;
import com.starsailor.components.PositionComponent;
import com.starsailor.components.RotationComponent;
import com.starsailor.util.GraphicsUtil;

import static com.starsailor.util.Settings.PPM;

public class PositionSystem extends AbstractIteratingSystem {
  public PositionSystem() {
    super(Family.all(PositionComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
    BodyComponent bodyComponent = entity.getComponent(BodyComponent.class);
    RotationComponent rotationComponent = entity.getComponent(RotationComponent.class);

    if(entity instanceof Spine){
      Spine spine = (Spine) entity;
      Vector2 position = bodyComponent.getWorldPosition();
      float bodyAngle = bodyComponent.body.getAngle();
      Vector2 targetVector = new Vector2();
      GraphicsUtil.angleToVector(targetVector, bodyAngle);
      rotationComponent.setRotationTarget(targetVector.x*PPM, targetVector.y*PPM);
      positionComponent.setPosition(position);
      spine.skeleton.setPosition(positionComponent.x, positionComponent.y);

      //TODO
      if(spine instanceof NPC && ((NPC)spine).selectionComponent.selected) {
        Vector2 center = spine.getCenter();
        ((NPC)spine).spriteComponent.setSelectionMarkerAt(center);
      }
    }
  }
}