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

import java.util.Collections;
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
  private Ship actualHit;

  public Bullet(WeaponProfile weaponProfile, Ship owner, Ship target) {
    shootingTime = System.currentTimeMillis();

    this.weaponProfile = weaponProfile;
    this.origin = owner.getCenter();
    this.owner = owner;
    this.target = target;

    createComponents(weaponProfile);

    playFiringSound();
  }

  /**
   * Returns true if this bullet has hit an actual enemy
   * and not accidently a member of a friendly formation.
   */
  public boolean wasFriendlyFire() {
    if(actualHit == null) {
      return false;
    }

    List<Ship> formationMembers = owner.formationComponent.getMembers();
    for(Ship formationMember : formationMembers) {
      //the target may differ from the actual hit
      if(formationMember.equals(actualHit)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Create components, etc.
   * @return true if the target body was not destroyed while this bullet was created
   */
  abstract public boolean create();

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
   * Returns the list of weapons that can defend this kind of bullet.
   * @return a list of weapon profiles that may destroy this bullet.
   */
  public List<WeaponProfile.Types> getDefensiveWeapons() {
    return Collections.emptyList();
  }

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
    markForDestroy();
    SoundManager.playSoundAtPosition(sound, 1f, new Vector3(position, 0));
    particleComponent.enabled = true;
  }

  /**
   * All default components of a bullet.
   * @param weaponProfile
   */
  protected void createComponents(WeaponProfile weaponProfile) {
    spriteComponent = ComponentFactory.addSpriteComponent(this, weaponProfile.sprite, 90);
    positionComponent = ComponentFactory.addPositionComponent(this);
    positionComponent.setPosition(owner.getCenter());
    bulletDamageComponent = ComponentFactory.addBulletDamageComponent(this, weaponProfile);
    particleComponent = ComponentFactory.addParticleComponent(this, weaponProfile.collisionEffect);

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
      //e.g. phaser don't have a body, so they don't have an impact which is o.k.
    }
  }

  //------------------ Helper --------------------------------------------------

  /**
   * Fires the firing sound configured in json
   */
  private void playFiringSound() {
    SoundManager.playSoundAtPosition(weaponProfile.sound, 0.5f, new Vector3(owner.getCenter().x, owner.getCenter().y, 0));
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
    return spriteComponent.getSprite(weaponProfile.sprite);
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

  public void setActualHit(Ship actualHit) {
    this.actualHit = actualHit;
  }

  /**
   * Checks if this bullet hit a groupmember of the given npc.
   */
  public boolean attackedMemberOf(NPC npc) {
    List<Ship> members = target.formationComponent.getMembers();
    for(Ship enemyMember : members) {
      if(enemyMember.equals(npc)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the body distance to the given bullet.
   */
  public float getDistanceTo(Bullet anotherBullet) {
    return bodyComponent.body.getPosition().dst(anotherBullet.bodyComponent.body.getPosition());
  }
}
