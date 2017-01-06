package com.nima.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.nima.components.SpriteComponent;

/**
 * Used during fighting
 */
public class ShootingSystem extends IteratingSystem {

  public ShootingSystem() {
    super(Family.all(SpriteComponent.class).get());
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    Entity eBullet = new Entity();
//    RenderableComponent renderComponent = engine.createComponent(RenderableComponent.class);
//
//    PositionComponent positionComponent = engine.createComponent(PositionComponent.class);
//    positionComponent.x = sprite.getX() + sprite.getWidth() / 2;
//    positionComponent.y = sprite.getY() + sprite.getHeight() / 2;
//    positionComponent.z = 900;
//
//    SpriteComponent     spriteComponent   = engine.createComponent(SpriteComponent.class);
//    spriteComponent.addTextures(new Texture("Entities/Actors/laser.png"));
//
//    BulletDamageComponent damageComponent = engine.createComponent(BulletDamageComponent.class);
//    damageComponent.damage = 10;
//
//    BulletCollisionComponent bulletColCom = new BulletCollisionComponent();
//
//    TypeComponent typeComponent     = engine.createComponent(TypeComponent.class);
//    typeComponent.type              = PhysicsManager.COL_FRIENDLY_BULLET;
//
//    BodyComponent bodyComponent     = engine.createComponent(BodyComponent.class);
//    bodyComponent.setBodyAndPosition(positionComponent,
//        BodyGenerator.generateBody(eBullet,
//            spriteComponent.sprites.first(),
//            Gdx.files.internal("Entities/BodyDefinitions/Laser.json"),
//            (short) (PhysicsManager.FRIENDLY_BITS | PhysicsManager.NEUTRAL_BITS)));
//
//    eBullet.add(positionComponent).add(spriteComponent).add(bodyComponent).add(renderComponent)
//        .add(damageComponent).add(bulletColCom).add(typeComponent);
//
//    Body bullet = bodyComponent.body;
//    Sprite bSprite = spriteComponent.sprites.first();
//    bullet.setTransform(bullet.getPosition().x, bullet.getPosition().y, (float) Math.toRadians(angle - 90f));
//    bullet.applyLinearImpulse(new Vector2(0.004f * (float) Math.sin(Math.toRadians(-angle)),
//        0.004f * (float) Math.cos(Math.toRadians(angle))), bullet.getPosition(), true);
//    bSprite.setRotation((float) Math.toDegrees(bullet.getAngle()));
//
//    shootingMap.get(entity).lastBulletTime = PhysicsShmup.currentTimeMillis;
//
//    engine.addEntity(eBullet);
//    EntityManager.add(eBullet);
  }
}
