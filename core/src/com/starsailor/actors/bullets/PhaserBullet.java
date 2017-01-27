package com.starsailor.actors.bullets;

import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.Ship;
import com.starsailor.components.SpriteComponent;
import com.starsailor.components.collision.BulletCollisionComponent;
import com.starsailor.data.WeaponProfile;
import com.starsailor.managers.EntityManager;
import com.starsailor.managers.TextureManager;
import com.starsailor.managers.Textures;

import java.util.Random;

/**
 * Concrete implementation of a weapon type.
 */
public class PhaserBullet extends Bullet {
  public PhaserBullet(WeaponProfile weaponProfile, Ship owner, Ship target) {
    super(weaponProfile, owner, target);
  }

  @Override
  protected void create() {
    //the bullet is already at the target
    positionComponent.setPosition(target.positionComponent.getPosition());
    particleComponent.enabled = true;
    spriteComponent.getSprite(Textures.PHASER).setTexture(true);
  }

  @Override
  public void update() {
    Vector2 sourcePos = owner.getCenter();
    Vector2 targetPos = target.getCenter();

    //check if shooting must be stopped
    if(isExhausted()) {
      EntityManager.getInstance().destroy(this);
      return;
    }

    SpriteComponent.SpriteItem spriteItem = getSpriteItem();

    //calculate angle between two instances
    Vector2 toTarget = new Vector2(targetPos).sub(sourcePos);
    float desiredAngle = (float) Math.atan2(-toTarget.x, toTarget.y);
    spriteItem.setRotation((float) Math.toDegrees(desiredAngle)-90);

    //calculate the center position between source and target as sprite position
    spriteItem.setPosition(targetPos, false);

    //scale the sprite to the desired width
    float distance = sourcePos.dst(targetPos);
    spriteItem.setWidth(distance);

    //TODO phaser animation
    Random random = new Random();
    int i = random.nextInt(3 - 1 + 1) + 1;
    if(i == 1) {
      spriteItem.getSprite().setTexture(TextureManager.getInstance().getTexture(Textures.PHASER));
    }
    if(i == 2) {
      spriteItem.getSprite().setTexture(TextureManager.getInstance().getTexture(Textures.PHASER_2));
    }
    if(i == 3) {
      spriteItem.getSprite().setTexture(TextureManager.getInstance().getTexture(Textures.PHASER_1));
    }

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
