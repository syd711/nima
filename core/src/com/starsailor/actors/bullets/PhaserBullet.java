package com.starsailor.actors.bullets;

import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.Ship;
import com.starsailor.components.AnimationComponent;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.collision.BulletCollisionComponent;
import com.starsailor.data.WeaponProfile;
import com.starsailor.managers.Textures;

/**
 * Concrete implementation of a weapon type.
 */
public class PhaserBullet extends Bullet {

  private AnimationComponent animationComponent;

  public PhaserBullet(WeaponProfile weaponProfile, Ship owner, Ship target) {
    super(weaponProfile, owner, target);
  }

  @Override
  public boolean create() {
    //the bullet is already at the target
    positionComponent.setPosition(target.positionComponent.getPosition());
    particleComponent.enabled = true;
    animationComponent = ComponentFactory.addAnimationComponent(this, Textures.PHASER, Textures.PHASER_1, Textures.PHASER_2, Textures.PHASER_3);
    animationComponent.setWrappedRepeat();

    return true;
  }

  @Override
  public void update() {
    //check if shooting must be stopped
    if(isExhausted()) {
      markForDestroy();
      return;
    }

    if(target.isMarkedForDestroy()) {
      markForDestroy();
      return;
    }

    Vector2 sourcePos = owner.getCenter();
    Vector2 targetPos = target.getCenter();

    //calculate angle between two instances
    Vector2 toTarget = new Vector2(targetPos).sub(sourcePos);
    float desiredAngle = (float) Math.atan2(-toTarget.x, toTarget.y);
    animationComponent.setRotation((float) Math.toDegrees(desiredAngle)-90);

    //calculate the center position between source and target as sprite position
    animationComponent.setPosition(targetPos);

    //scale the sprite to the desired width
    float distance = sourcePos.dst(targetPos);
    animationComponent.setWidth(distance);

    //apply permanent collision
    BulletCollisionComponent bulletCollisionComponent = getComponent(BulletCollisionComponent.class);
    bulletCollisionComponent.applyCollisionWith(this, target, target.getCenter());

    //update positions
    positionComponent.setPosition(target.positionComponent.getPosition());
    particleComponent.effect.setPosition(positionComponent.x, positionComponent.y);
  }

  @Override
  public void collide(Ship ship, Vector2 position) {
    updateDamage(ship);
  }

  @Override
  public void collide(Bullet bullet, Vector2 position) {

  }
}
