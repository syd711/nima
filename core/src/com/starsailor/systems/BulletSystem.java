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
import com.starsailor.util.Box2dUtil;
import com.starsailor.util.GraphicsUtil;

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

      spriteComponent.sprite.setRotation((float) Math.toDegrees(bodyComponent.body.getAngle()));

      //TODO update rotation

      if(bullet.is(DataEntities.WEAPON_LASER)) {
        //nothing
      }
      else if(bullet.is(DataEntities.WEAPON_MISSILE) && bullet.target != null) {
        float distanceToPlayer = bullet.getDistanceToPlayer();
        if(distanceToPlayer > weaponProfile.activationDistance && !bullet.steerableComponent.isEnabled()) {
          bullet.steerableComponent.setEnabled(true);
        }
        else {
          Body body = bullet.bodyComponent.body;
          float forceValue = 0.01f;

          Vector2 pos = bullet.owner.bodyComponent.body.getPosition();
          //0 degree is in the east, but we want north as 0
          Vector2 delta = GraphicsUtil.getDelta((float) Math.toDegrees(bullet.owner.bodyComponent.body.getAngle())-90, 1);
          body.applyForceToCenter(delta.scl(forceValue), true);

          body.setTransform(body.getPosition(), Box2dUtil.rotateDegrees(-90, bullet.owner.bodyComponent.body.getAngle(), true));
        }
      }
    }
  }
}