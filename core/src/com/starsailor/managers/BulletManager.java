package com.starsailor.managers;

import com.starsailor.actors.Ship;
import com.starsailor.actors.bullets.*;
import com.starsailor.data.WeaponProfile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Creates new bullets.
 */
public class BulletManager {

  private static BulletManager instance;

  private LinkedList<QueuedBullet> delayedBulletsStack = new LinkedList<>();

  public static BulletManager getInstance() {
    if(instance == null) {
      instance = new BulletManager();
    }
    return instance;
  }

  public void create(Ship owner, Ship target, WeaponProfile weaponProfile) {
    create(owner, target, weaponProfile, false);
  }

  private void create(Ship owner, Ship target, WeaponProfile weaponProfile, boolean internal) {
    WeaponProfile.Types type = weaponProfile.type;
    int bulletCount = weaponProfile.bulletCount;

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
      case ROCKET: {
        if(!internal) {
          fireRockets(weaponProfile, owner, target);
        }
        else {
          //only called via delayed queue
          bullet = new RocketBullet(weaponProfile, owner, target);
        }
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
        for(int i = 1; i < bulletCount; i++) {
          Bullet flaresBullet = new FlaresBullet(weaponProfile, owner, target);
          enableBullet(flaresBullet, weaponProfile);
        }
        break;
      }
      default: {
        throw new UnsupportedOperationException("Unsupported weapon type " + type.toString());
      }
    }

    if(bullet != null) {
      enableBullet(bullet, weaponProfile);
    }
  }

  /**
   * Updated to check delayed bullets firing
   * @param deltaTime
   */
  public void update(float deltaTime) {
    if(!delayedBulletsStack.isEmpty()) {
      long time = System.currentTimeMillis();

      List<QueuedBullet> queueCopy = new ArrayList<>(delayedBulletsStack);
      for(QueuedBullet bullet : queueCopy) {
        if(bullet.shootingTime < time) {
          create(bullet.owner, bullet.target, bullet.weaponProfile, true);
          delayedBulletsStack.remove(bullet);
        }
      }
    }
  }

  // ---------------------------- Helper --------------------------------------------

  /**
   * Rockets are a little bit more complex.
   * They try to aim for all targets of the enemy group while bullets available.
   */
  private void fireRockets(WeaponProfile weaponProfile, Ship owner, Ship target) {
    int bulletCount = weaponProfile.bulletCount;
    List<Ship> members = target.formationComponent.getMembers();
    Iterator<Ship> iterator = members.iterator();
    Ship nextTarget = null;
    long time = System.currentTimeMillis();

    for(int i = 0; i < bulletCount; i++) {
      if(!iterator.hasNext()) {
        iterator = members.iterator();
      }

      nextTarget = iterator.next();
      QueuedBullet queuedBullet = new QueuedBullet(owner, nextTarget, weaponProfile, (long) (time + weaponProfile.bulletDelay));
      delayedBulletsStack.add(queuedBullet);
    }
  }

  /**
   * Additonal bullet creation stuff
   */
  private void enableBullet(Bullet bullet, WeaponProfile weaponProfile) {
    bullet.create();
    EntityManager.getInstance().add(bullet);
    bullet.owner.shootingComponent.updateLastBulletTime(weaponProfile);
  }

  class QueuedBullet {
    Ship owner;
    Ship target;
    WeaponProfile weaponProfile;
    long shootingTime;

    public QueuedBullet(Ship owner, Ship target, WeaponProfile weaponProfile, long shootingTime) {
      this.owner = owner;
      this.target = target;
      this.weaponProfile = weaponProfile;
      this.shootingTime = shootingTime;
    }
  }
}
