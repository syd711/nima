package com.starsailor.actors.bullets;

import com.starsailor.actors.Ship;
import com.starsailor.data.WeaponProfile;
import com.starsailor.managers.EntityManager;

/**
 * Creates new bullets via reflection.
 */
public class BulletFactory {

  public static void create(Ship owner, Ship target, WeaponProfile weaponProfile) {
    WeaponProfile.Types type = weaponProfile.type;
    Bullet bullet = null;
    switch(type) {
      case LASER: {
        bullet = new LaserBullet(weaponProfile, owner, target);
        break;
      }
      case MISSILE: {
        bullet = new MissileBullet(weaponProfile, owner, target);
        break;
      }
      case PHASER: {
        bullet = new PhaserBullet(weaponProfile, owner, target);
        break;
      }
      case MINE: {
        bullet = new MineBullet(weaponProfile, owner, target);
        break;
      }
      case FLARES: {
        bullet = new FlaresBullet(weaponProfile, owner, target);
        break;
      }
      default: {
        throw new UnsupportedOperationException("Unsupported weapon type " + type.toString());
      }
    }


    bullet.create();
    EntityManager.getInstance().add(bullet);
    bullet.owner.shootingComponent.updateLastBulletTime(weaponProfile);
  }
}
