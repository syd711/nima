package com.nima.actors;

import com.nima.managers.EntityManager;

/**
 * Entity for bullets
 */
public class Bullet extends Sprite {
  public static Bullet newBullet() {
    return new Bullet("sprites/laser.png");
  }

  private Bullet(String resourceLocation) {
    super(resourceLocation);
    EntityManager.getInstance().addBulletDamageComponent(this, 10);
  }
}
