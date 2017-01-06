package com.nima.actors;

import com.badlogic.gdx.math.Vector2;
import com.nima.components.BulletDamageComponent;
import com.nima.components.ComponentFactory;

/**
 * Entity for bullets
 */
public class Bullet extends Sprite {
  public final BulletDamageComponent bulletDamageComponent;

  public static Bullet newBullet(Vector2 position) {
    return new Bullet("sprites/laser.png", position);
  }

  private Bullet(String resourceLocation, Vector2 position) {
    super(resourceLocation, position);
    bulletDamageComponent = ComponentFactory.addBulletDamageComponent(this, 10);
  }
}
