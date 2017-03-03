package com.starsailor.managers;

import com.starsailor.actors.Ship;
import com.starsailor.actors.bullets.*;
import com.starsailor.model.WeaponData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Creates new bullets.
 */
public class BulletManager {

  private static BulletManager instance;

  private ConcurrentLinkedQueue<QueuedBullet> delayedBulletsQueue = new ConcurrentLinkedQueue<>();

  public static BulletManager getInstance() {
    if(instance == null) {
      instance = new BulletManager();
    }
    return instance;
  }

  public void create(Ship owner, Ship target, WeaponData weaponData) {
    create(owner, target, weaponData, false);
  }

  private void create(Ship owner, Ship target, WeaponData weaponData, boolean internal) {
    WeaponData.Types type = weaponData.getWeaponType();
    int bulletCount = weaponData.getBulletCount();

    Bullet bullet = null;
    switch(type) {
      case LASER: {
        bullet = new LaserBullet(weaponData, owner, target);
        break;
      }
      case MISSILE: {
        if(!internal) {
          fireMissiles(weaponData, owner, target);
        }
        else {
          //only called via delayed queue
          bullet = new MissileBullet(weaponData, owner, target);
        }
        break;
      }
      case PHASER: {
        bullet = new PhaserBullet(weaponData, owner, target);
        break;
      }
      case MINE: {
        bullet = new MineBullet(weaponData, owner, target);
        break;
      }
      case FLARES: {
        bullet = new FlaresBullet(weaponData, owner, target);
        for(int i = 1; i < bulletCount; i++) {
          Bullet flaresBullet = new FlaresBullet(weaponData, owner, target);
          enableBullet(flaresBullet, weaponData);
        }
        break;
      }
      default: {
        throw new UnsupportedOperationException("Unsupported weapon type " + type.toString());
      }
    }

    if(bullet != null) {
      enableBullet(bullet, weaponData);
    }
  }

  /**
   * Updated to check delayed bullets firing
   * @param deltaTime
   */
  public void update(float deltaTime) {
    if(!delayedBulletsQueue.isEmpty()) {
      long time = System.currentTimeMillis();

      List<QueuedBullet> queueCopy = new ArrayList<>(delayedBulletsQueue);
      for(QueuedBullet bullet : queueCopy) {
        if(bullet.shootingTime < time) {
          //only create if there still alive
          if(!bullet.owner.isMarkedForDestroy()) {
            create(bullet.owner, bullet.target, bullet.weaponData, true);
          }

          delayedBulletsQueue.remove(bullet);
        }
      }
    }
  }

  // ---------------------------- Helper --------------------------------------------

  /**
   * Rockets are a little bit more complex.
   * They try to aim for all targets of the enemy group while bullets available.
   */
  private void fireMissiles(WeaponData weaponData, Ship owner, Ship target) {
    int bulletCount = weaponData.getBulletCount();
    if(bulletCount == 0) {
      bulletCount = 1;
    }
    List<Ship> members = target.formationComponent.getMembers();
    Iterator<Ship> iterator = members.iterator();
    Ship nextTarget = null;
    long time = System.currentTimeMillis();
    long shootingTime = time + weaponData.getBulletDelay();

    //queue primary target first
    QueuedBullet queuedBullet = new QueuedBullet(owner, target, weaponData, shootingTime);
    delayedBulletsQueue.add(queuedBullet);

    for(int i = 1; i < bulletCount; i++) {
      if(!iterator.hasNext()) {
        iterator = members.iterator();
      }

      nextTarget = iterator.next();
      shootingTime = time + weaponData.getBulletDelay()*(i+1);

      queuedBullet = new QueuedBullet(owner, nextTarget, weaponData, shootingTime);
      delayedBulletsQueue.add(queuedBullet);
    }

    owner.shootingComponent.updateLastBulletTime(weaponData);
  }

  /**
   * Additonal bullet creation stuff
   */
  private void enableBullet(Bullet bullet, WeaponData weaponData) {
    if(bullet.create()) {
      EntityManager.getInstance().add(bullet);
      bullet.owner.shootingComponent.updateLastBulletTime(weaponData);
    }
    else {
      bullet.markForDestroy();
    }
  }

  class QueuedBullet {
    Ship owner;
    Ship target;
    WeaponData weaponData;
    long shootingTime;

    public QueuedBullet(Ship owner, Ship target, WeaponData weaponData, long shootingTime) {
      this.owner = owner;
      this.target = target;
      this.weaponData = weaponData;
      this.shootingTime = shootingTime;
    }
  }
}
