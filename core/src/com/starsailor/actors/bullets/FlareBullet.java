package com.starsailor.actors.bullets;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.starsailor.actors.Ship;
import com.starsailor.data.WeaponProfile;
import com.starsailor.managers.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Concrete implementation of a weapon type.
 */
public class FlareBullet extends Bullet {
  public FlareBullet(WeaponProfile weaponProfile, Ship owner, Ship target) {
    super(weaponProfile, owner, target);
  }

  @Override
  protected void create() {
    List<Bullet> bullets = new ArrayList<>();
    for(int i=0; i<weaponProfile.bulletCount-1; i++) {
      //Bullet b = BulleBullet(weaponProfile, owner, target);
      //bullets.add(b);
      //EntityManager.getInstance().add(b);
    }
    bullets.add(this);

    for(com.starsailor.actors.bullets.Bullet b : bullets) {
      Body bb = b.bodyComponent.body;
      Random r = new Random();
      int angleDegree = r.nextInt((180 - (-180))) + (-180);

      float angle = (float) Math.toRadians(angleDegree);
      Vector2 force = new Vector2();
      force.x = (float) Math.cos(angle);
      force.y = (float) Math.sin(angle);
      force = force.scl(weaponProfile.forceFactor);

      bb.applyForceToCenter(force, true);
      bb.applyTorque(weaponProfile.torque, true);
    }

    float angularVelocity = bodyComponent.body.getAngularVelocity();
    if(angularVelocity > 0 && angularVelocity<0.5f) {
      EntityManager.getInstance().destroy(this);
    }
  }

  @Override
  public void update() {
    updateSpritePositionForBody(true);
  }

  @Override
  protected void collide(Vector2 position) {

  }
}
