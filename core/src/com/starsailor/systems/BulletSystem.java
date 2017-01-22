package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.starsailor.actors.Bullet;
import com.starsailor.components.BodyComponent;
import com.starsailor.data.WeaponProfile;

//TODO split into position system and spriterendersystem
public class BulletSystem extends AbstractIteratingSystem {
  public BulletSystem() {
    super(Family.all(BodyComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    if(entity instanceof Bullet) {
      Bullet bullet = (Bullet) entity;
      WeaponProfile weaponProfile = bullet.weaponProfile;

      if(bullet.is(WeaponProfile.Types.LASER)) {
        //nothing
      }
      else if(bullet.is(WeaponProfile.Types.MISSILE) && bullet.target != null) {
        float distanceToPlayer = bullet.getDistanceFromOrigin();
        if(distanceToPlayer > weaponProfile.activationDistance && !bullet.steerableComponent.isEnabled()) {
          bullet.steerableComponent.setEnabled(true);
        }
      }
    }
  }
}