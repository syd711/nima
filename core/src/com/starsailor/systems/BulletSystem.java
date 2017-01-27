package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.components.BulletDamageComponent;

public class BulletSystem extends AbstractIteratingSystem {
  public BulletSystem() {
    super(Family.all(BulletDamageComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    Bullet bullet = (Bullet) entity;
    bullet.update();
  }
}