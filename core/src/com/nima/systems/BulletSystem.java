package com.nima.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.MassData;
import com.nima.actors.Bullet;
import com.nima.components.BodyComponent;
import com.nima.components.PositionComponent;
import com.nima.components.SpriteComponent;
import com.nima.data.DataEntities;

import static com.nima.util.Settings.PPM;

public class BulletSystem extends AbstractIteratingSystem {
  public BulletSystem() {
    super(Family.all(BodyComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    if(entity instanceof Bullet) {
      Bullet bullet = (Bullet) entity;

      SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
      PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
      BodyComponent bodyComponent = entity.getComponent(BodyComponent.class);

      // Position priority: Body => PositionComponent => Sprites  (highest to lowest)
      positionComponent.x = bodyComponent.body.getPosition().x * PPM - spriteComponent.sprite.getWidth() / 2;
      positionComponent.y = bodyComponent.body.getPosition().y * PPM - spriteComponent.sprite.getHeight() / 2;

      spriteComponent.sprite.setX(positionComponent.x);
      spriteComponent.sprite.setY(positionComponent.y);

      if(bullet.is(DataEntities.WEAPON_LASER)) {
        //nothing
      }
      else if(bullet.is(DataEntities.WEAPON_MISSILE) && bullet.target != null) {
        float G = 1f; //modifier of gravity value - you can make it bigger to have stronger gravity

        Body body = bullet.bodyComponent.body;
        MassData data = new MassData();
        data.mass = 10;
//        body.setMassData(data);
        Vector2 aimingVector = bullet.target.positionComponent.getBox2dPosition();
        float distance = body.getPosition().dst(aimingVector);
        float forceValue = G / (distance * distance);
//        float forceValue = 0.00004f;

        Vector2 direction = aimingVector.sub(body.getPosition());
        body.applyForce(direction.scl(forceValue), body.getWorldCenter(), true);

        body.setTransform(body.getPosition(), aimingVector.angleRad());
      }
    }
  }
}