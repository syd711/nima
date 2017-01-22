package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.starsailor.actors.Bullet;
import com.starsailor.components.BodyComponent;
import com.starsailor.data.DataEntities;
import com.starsailor.data.WeaponProfile;
import com.starsailor.util.GraphicsUtil;

//TODO split into position system and spriterendersystem
public class BulletSystem extends AbstractIteratingSystem {
  public BulletSystem() {
    super(Family.all(BodyComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    if(entity instanceof Bullet) {
      Bullet bullet = (Bullet) entity;
      WeaponProfile weaponProfile = bullet.weaponProfile;

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
          Vector2 delta = GraphicsUtil.getDelta((float) Math.toDegrees(body.getAngle())-90, 1);
          body.applyForceToCenter(delta.scl(forceValue), true);
        }
      }
    }
  }
}