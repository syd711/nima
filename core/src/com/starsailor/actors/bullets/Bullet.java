package com.starsailor.actors.bullets;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.starsailor.Game;
import com.starsailor.actors.GameEntity;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Player;
import com.starsailor.actors.Ship;
import com.starsailor.components.*;
import com.starsailor.data.WeaponProfile;
import com.starsailor.managers.*;

import java.util.List;

import static com.starsailor.util.Settings.PPM;

/**
 * Entity for bullets
 */
abstract public class Bullet extends GameEntity {
  public SpriteComponent spriteComponent;
  public PositionComponent positionComponent;
  public SteerableComponent steerableComponent;
  public BulletDamageComponent bulletDamageComponent;
  public ParticleComponent particleComponent;
  public BodyComponent bodyComponent;

  public WeaponProfile weaponProfile;
  public Ship owner;
  public Ship target;

  private Vector2 origin;
  private long shootingTime = 0;

  public Bullet(WeaponProfile weaponProfile, Ship owner, Ship target) {
    shootingTime = System.currentTimeMillis();

    this.weaponProfile = weaponProfile;
    this.origin = owner.getCenter();
    this.owner = owner;
    this.target = target;

    createComponents(weaponProfile);

    playFiringSound();
  }

  public boolean isFriendlyFire() {
    List<NPC> formationMembers = owner.formationComponent.getMembers();
    for(NPC formationMember : formationMembers) {
      if(formationMember.equals(target)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Create components, etc.
   */
  abstract protected void create();

  /**
   * Called by the bullet system to update the bullet
   */
  abstract public void update();

  /**
   * Called by the collision component when a collision happens
   * @param position position of the collision
   */
  abstract public void collide(Ship ship, Vector2 position);

  /**
   * Called by the collision component when a collision happens
   * @param position position of the collision
   */
  abstract public void collide(Bullet bullet, Vector2 position);

  //------------------ Possible overwrites  ----------------------------------

  /**
   * Checks if the ship is destroyed so that the selection
   * is resetted and an explosion animation is rendered.
   */
  protected void updateDamage(Ship ship) {
    ship.applyDamageFor(this);
  }

  /**
   * Play sound, play effect and remove the entity from the system
   */
  protected void hitAndDestroyBullet(Vector2 position, String sound) {
    EntityManager.getInstance().destroy(this);
    SoundManager.playSoundAtPosition(sound, 1f, new Vector3(position, 0));
    particleComponent.enabled = true;
  }

  /**
   * All default components of a bullet.
   * @param weaponProfile
   */
  protected void createComponents(WeaponProfile weaponProfile) {
    spriteComponent = ComponentFactory.addSpriteComponent(this, Textures.valueOf(weaponProfile.type.name().toUpperCase()), 90);
    positionComponent = ComponentFactory.addPositionComponent(this);
    positionComponent.setPosition(owner.getCenter());
    bulletDamageComponent = ComponentFactory.addBulletDamageComponent(this, weaponProfile);
    particleComponent = ComponentFactory.addParticleComponent(this, Particles.valueOf(weaponProfile.name.toUpperCase() + "_BULLET_HIT"));

    //not all bullets require a body
    if(weaponProfile.bodyData != null) {
      bodyComponent = ComponentFactory.addBulletBodyComponent(this, owner.getCenter(), weaponProfile, owner instanceof Player);
    }

    ComponentFactory.addBulletCollisionComponent(this);
  }

  /**
   * Apply force to the box2d so that the impact is visible
   */
  public void applyImpactForce(Ship ship, Vector2 position) {
    BodyComponent component = ship.getComponent(BodyComponent.class);
    float impactFactor = weaponProfile.impactFactor;

    if(bodyComponent != null) {
      //use my linear velocity
      Vector2 linearVelocity = bodyComponent.body.getLinearVelocity();
      Vector2 force = new Vector2(linearVelocity.x * impactFactor, linearVelocity.y * impactFactor);
      //to apply it on the target
      component.body.applyForceToCenter(force.x, force.y, true);
    }
    else {
      //TODO e.g. phaser
      //component.body.applyForceToCenter(force.x, force.y, true);
    }
  }

  //------------------ Helper --------------------------------------------------

  /**
   * Fires the firing sound configured in json
   */
  private void playFiringSound() {
    Sounds sound = Sounds.valueOf(weaponProfile.sound.toUpperCase());
    SoundManager.playSoundAtPosition(sound, 0.5f, new Vector3(owner.getCenter().x, owner.getCenter().y, 0));
  }

  /**
   * For bullets that use box2d and have to follow the body.
   */
  protected void updateSpritePositionForBody(boolean updateAngle) {
    SpriteComponent.SpriteItem spriteItem = getSpriteItem();
    Sprite bulletSprite = spriteItem.getSprite();
    positionComponent.x = bodyComponent.body.getPosition().x * PPM - bulletSprite.getWidth() / 2;
    positionComponent.y = bodyComponent.body.getPosition().y * PPM - bulletSprite.getHeight() / 2;

    spriteItem.setPosition(positionComponent.getPosition(), false);
    if(updateAngle) {
      spriteItem.setRotation((float) Math.toDegrees(bodyComponent.body.getAngle()));
    }
  }

  protected SpriteComponent.SpriteItem getSpriteItem() {
    return spriteComponent.getSprite(Textures.valueOf(weaponProfile.name.toUpperCase()));
  }

  public boolean is(WeaponProfile.Types type) {
    return weaponProfile.type.equals(type);
  }

  public boolean isOwner(Entity entity) {
    return owner.equals(entity);
  }

  public float getDistanceFromOrigin() {
    return positionComponent.getPosition().dst(origin);
  }

  public boolean isExhausted() {
    float current = Game.currentTimeMillis - shootingTime;
    return current > weaponProfile.durationMillis;
  }
}
