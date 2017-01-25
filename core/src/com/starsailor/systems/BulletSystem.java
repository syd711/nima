package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.Bullet;
import com.starsailor.actors.Ship;
import com.starsailor.components.BodyComponent;
import com.starsailor.components.PositionComponent;
import com.starsailor.components.SpriteComponent;
import com.starsailor.data.WeaponProfile;
import com.starsailor.managers.Textures;

import static com.starsailor.util.Settings.PPM;

//TODO split into position system and spriterendersystem
public class BulletSystem extends AbstractIteratingSystem {
  public BulletSystem() {
    super(Family.all(SpriteComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    if(entity instanceof Bullet) {
      Bullet bullet = (Bullet) entity;

      WeaponProfile weaponProfile = bullet.weaponProfile;
      PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
      BodyComponent bodyComponent = entity.getComponent(BodyComponent.class);
      SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
      SpriteComponent.SpriteItem spriteItem = spriteComponent.getSprite(Textures.valueOf(weaponProfile.name.toUpperCase()));

      if(bullet.is(WeaponProfile.Types.LASER)) {
        updateSpritePositionForBody(positionComponent, bodyComponent, spriteItem);
        spriteItem.setRotation((float) Math.toDegrees(bodyComponent.body.getAngle()));
      }
      else if(bullet.is(WeaponProfile.Types.MISSILE) && bullet.target != null) {
        updateSpritePositionForBody(positionComponent, bodyComponent, spriteItem);

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
      else if(bullet.is(WeaponProfile.Types.PHASER)) {
        Ship target = bullet.target;
        Ship source = bullet.owner;
        Vector2 sourcePos = source.positionComponent.getPosition();
        Vector2 targetPos = target.positionComponent.getPosition();

        //calculate angle between two instances
        Vector2 toTarget = new Vector2(targetPos).sub(sourcePos);
        float desiredAngle = (float) Math.atan2(-toTarget.x, toTarget.y);
        spriteItem.setRotation((float) Math.toDegrees(desiredAngle)-90);

        //calculate the center position between source and target as sprite position
        spriteItem.setPosition(targetPos, false);

        //scale the sprite to the desired width
        float distance = sourcePos.dst(targetPos);
        spriteItem.setWidth(distance);
      }
    }
  }

  /**
   * For bullets that use box2d and have to follow the body.
   * @param positionComponent
   * @param bodyComponent
   * @param spriteItem
   */
  private void updateSpritePositionForBody(PositionComponent positionComponent,
                                           BodyComponent bodyComponent,
                                           SpriteComponent.SpriteItem spriteItem) {
    Sprite bulletSprite = spriteItem.getSprite();
    positionComponent.x = bodyComponent.body.getPosition().x * PPM - bulletSprite.getWidth() / 2;
    positionComponent.y = bodyComponent.body.getPosition().y * PPM - bulletSprite.getHeight() / 2;

    spriteItem.setPosition(positionComponent.getPosition(), false);
  }
}