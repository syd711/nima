package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.Bullet;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Spine;
import com.starsailor.components.BodyComponent;
import com.starsailor.components.PositionComponent;
import com.starsailor.components.SpriteComponent;
import com.starsailor.util.GraphicsUtil;

import static com.starsailor.util.Settings.PPM;

public class PositionSystem extends AbstractIteratingSystem {
  public PositionSystem() {
    super(Family.all(PositionComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
    BodyComponent bodyComponent = entity.getComponent(BodyComponent.class);

    //check all entities that have a body and update their position and rotation according to their body
    if(entity instanceof Spine){
      Spine spine = (Spine) entity;
      Vector2 position = bodyComponent.getWorldPosition();
      float bodyAngle = bodyComponent.body.getAngle();
      Vector2 targetVector = new Vector2();
      GraphicsUtil.angleToVector(targetVector, bodyAngle);
      positionComponent.setPosition(position);
      spine.skeleton.setPosition(positionComponent.x, positionComponent.y);

      //TODO
      if(spine instanceof NPC && ((NPC)spine).selectionComponent.selected) {
        Vector2 center = spine.getCenter();
        ((NPC)spine).spriteComponent.setSelectionMarkerAt(center);
      }
    }
    else if(entity instanceof Bullet) {
      SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);

      // Position priority: Body => PositionComponent => Sprites  (highest to lowest)
      positionComponent.x = bodyComponent.body.getPosition().x * PPM - spriteComponent.sprite.getWidth() / 2;
      positionComponent.y = bodyComponent.body.getPosition().y * PPM - spriteComponent.sprite.getHeight() / 2;

      spriteComponent.sprite.setX(positionComponent.x);
      spriteComponent.sprite.setY(positionComponent.y);

      spriteComponent.sprite.setRotation((float) Math.toDegrees(bodyComponent.body.getAngle()));
    }
  }
}