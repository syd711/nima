package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.starsailor.actors.Bullet;
import com.starsailor.components.BodyComponent;
import com.starsailor.components.PositionComponent;
import com.starsailor.components.SpriteComponent;
import com.starsailor.data.WeaponProfile;
import com.starsailor.managers.Sprites;

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

      SpriteComponent.SpriteItem spriteItem = spriteComponent.getSprite(Sprites.valueOf(weaponProfile.name.toUpperCase()));
      Sprite bulletSprite = spriteItem.sprite;
      positionComponent.x = bodyComponent.body.getPosition().x * PPM - bulletSprite.getWidth() / 2;
      positionComponent.y = bodyComponent.body.getPosition().y * PPM - bulletSprite.getHeight() / 2;

      spriteItem.setPosition(positionComponent.getPosition(), false);

      if(bullet.is(WeaponProfile.Types.LASER)) {
        spriteItem.setRotation((float) Math.toDegrees(bodyComponent.body.getAngle()));
      }
      else if(bullet.is(WeaponProfile.Types.MISSILE) && bullet.target != null) {
        if(!bullet.steerableComponent.isDestroyed()) {
          float distanceToPlayer = bullet.getDistanceFromOrigin();
          //lazy init of the bullet's steering system
          if(distanceToPlayer > weaponProfile.activationDistance && !bullet.steerableComponent.isEnabled()) {
            bullet.steerableComponent.setEnabled(true);
          }
          else {
            spriteItem.setRotation((float) Math.toDegrees(bodyComponent.body.getAngle())-90);
          }
        }
      }
    }
  }
}