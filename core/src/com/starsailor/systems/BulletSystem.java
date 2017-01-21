package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.starsailor.actors.Bullet;
import com.starsailor.components.BodyComponent;
import com.starsailor.components.PositionComponent;
import com.starsailor.components.SpriteComponent;
import com.starsailor.data.DataEntities;
import com.starsailor.data.WeaponProfile;

import static com.starsailor.util.Settings.PPM;

//TODO split into position system and spriterendersystem
public class BulletSystem extends AbstractIteratingSystem {
  public BulletSystem() {
    super(Family.all(BodyComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    if(entity instanceof Bullet) {
      Bullet bullet = (Bullet) entity;
      WeaponProfile weaponProfile = bullet.weaponProfile;

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
        float distanceToPlayer = bullet.getDistanceToPlayer();
        if(distanceToPlayer > weaponProfile.activationDistance) {
          bullet.steerableComponent.setEnabled(true);
        }
        else {
          Body body = bullet.bodyComponent.body;
          Vector2 aimingVector = bullet.target.positionComponent.getBox2dPosition();
          float forceValue = 0.00004f;

          Vector2 direction = aimingVector.sub(body.getPosition());
          body.applyForce(direction.scl(forceValue), body.getWorldCenter(), true);

          body.setTransform(body.getPosition(), aimingVector.angleRad());
        }
      }
    }
  }
}