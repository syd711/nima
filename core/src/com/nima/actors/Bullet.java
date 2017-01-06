package com.nima.actors;

import com.nima.components.ComponentFactory;

/**
 * Entity for bullets
 */
public class Bullet extends Sprite {
  public static Bullet newBullet() {
    return new Bullet("sprites/laser.png");
  }

  private Bullet(String resourceLocation) {
    super(resourceLocation);
    ComponentFactory.addBulletDamageComponent(this, 10);
  }
}
