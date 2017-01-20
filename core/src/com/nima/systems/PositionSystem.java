package com.nima.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector2;
import com.nima.actors.NPC;
import com.nima.actors.Player;
import com.nima.actors.Spine;
import com.nima.components.BodyComponent;
import com.nima.components.PositionComponent;
import com.nima.components.RotationComponent;
import com.nima.util.GraphicsUtil;

import static com.nima.util.Settings.MPP;
import static com.nima.util.Settings.PPM;

public class PositionSystem extends AbstractIteratingSystem {
  public PositionSystem() {
    super(Family.all(PositionComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
    BodyComponent bodyComponent = entity.getComponent(BodyComponent.class);
    RotationComponent rotationComponent = entity.getComponent(RotationComponent.class);

    //TODO makes more sense to have both entity types moving by box2d
    if(entity instanceof Player) {
      Spine spine = (Spine) entity;
      spine.skeleton.setPosition(positionComponent.x, positionComponent.y);
      bodyComponent.body.setTransform(spine.getCenter().x*MPP, spine.getCenter().y*MPP, rotationComponent.getB2dAngle());
    }
    else if(entity instanceof NPC){
      NPC spine = (NPC) entity;
      Vector2 position = bodyComponent.getWorldPosition();
      float bodyAngle = bodyComponent.body.getAngle();
      Vector2 targetVector = new Vector2();
      GraphicsUtil.angleToVector(targetVector, bodyAngle);
      rotationComponent.setRotationTarget(targetVector.x*PPM, targetVector.y*PPM);
      positionComponent.setPosition(position);
      spine.skeleton.setPosition(positionComponent.x, positionComponent.y);

      if(spine.selectionComponent.selected) {
        Vector2 center = spine.getCenter();
        spine.spriteComponent.setSelectionMarkerAt(center);
      }
    }
  }
}