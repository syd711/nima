package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.starsailor.actors.Bullet;
import com.starsailor.components.BodyComponent;
import com.starsailor.components.PositionComponent;
import com.starsailor.components.SpriteComponent;
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
      PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
      BodyComponent bodyComponent = entity.getComponent(BodyComponent.class);
      SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);

      positionComponent.x = bodyComponent.body.getPosition().x * PPM - spriteComponent.sprite.getWidth() / 2;
      positionComponent.y = bodyComponent.body.getPosition().y * PPM - spriteComponent.sprite.getHeight() / 2;

      spriteComponent.sprite.setX(positionComponent.x);
      spriteComponent.sprite.setY(positionComponent.y);

      if(bullet.is(WeaponProfile.Types.LASER)) {
        spriteComponent.sprite.setRotation((float) Math.toDegrees(bodyComponent.body.getAngle()));
      }
      else if(bullet.is(WeaponProfile.Types.MISSILE) && bullet.target != null) {
        float distanceToPlayer = bullet.getDistanceFromOrigin();
        if(distanceToPlayer > weaponProfile.activationDistance && !bullet.steerableComponent.isEnabled()) {
          bullet.steerableComponent.setEnabled(true);
        }
        else {
          spriteComponent.sprite.setRotation((float) Math.toDegrees(bodyComponent.body.getAngle())-90);
        }
      }
    }
  }
}