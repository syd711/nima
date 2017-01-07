package com.nima.actors;

import com.badlogic.gdx.math.Vector2;
import com.nima.components.BulletDamageComponent;
import com.nima.components.ComponentFactory;
import com.nima.util.Settings;

/**
 * Entity for bullets
 */
public class Bullet extends Sprite {
  public final BulletDamageComponent bulletDamageComponent;
  private final static String NAME = "laser";

  public static Bullet newBullet(Vector2 position) {
    return new Bullet(NAME, position);
  }

  private Bullet(String name, Vector2 position) {
    super(name, position);
    bulletDamageComponent = ComponentFactory.addBulletDamageComponent(this, 10);
    bodyComponent = ComponentFactory.addBodyComponent(this, name, (short) (Settings.FRIENDLY_BITS | Settings.NEUTRAL_BITS), position, spriteComponent.sprite);
  }
}
