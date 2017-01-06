package com.nima.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nima.Game;
import com.nima.actors.Bullet;
import com.nima.actors.Player;
import com.nima.components.BodyComponent;
import com.nima.components.PositionComponent;
import com.nima.components.ShootingComponent;
import com.nima.managers.EntityManager;

/**
 * Used during fighting
 */
public class ShootingSystem extends IteratingSystem {
  private ComponentMapper<ShootingComponent> shootingMap     = ComponentMapper.getFor(ShootingComponent.class);

  public ShootingSystem() {
    super(Family.all(ShootingComponent.class).get());
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    int bulletDelayMillis = 350;
    long   lastBulletTime = shootingMap.get(entity).lastBulletTime;

    if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
      if (Game.currentTimeMillis - lastBulletTime > bulletDelayMillis) {
        Bullet eBullet = new Bullet();
        Sprite sprite = EntityManager.getInstance().addSpriteComponent(eBullet, "sprites/laser.png").sprite;
        EntityManager.getInstance().addBulletDamageComponent(eBullet, 10);
        PositionComponent positionComponent = EntityManager.getInstance().addPositionComponent(eBullet, true, sprite.getHeight());
        BodyComponent bodyComponent = EntityManager.getInstance().addBodyComponent(eBullet, positionComponent, sprite);

        //TODO dont use player here
        Vector2 toTarget = Player.getInstance().getComponent(BodyComponent.class).body.getPosition().sub(bodyComponent.body.getPosition());
        float angle = (float) Math.atan2(-toTarget.x, toTarget.y);
        angle = (float) Math.toDegrees(angle);

        Body bullet = bodyComponent.body;
        bullet.setTransform(bullet.getPosition().x, bullet.getPosition().y, (float) Math.toRadians(angle - 90f));
        bullet.applyLinearImpulse(new Vector2(0.004f * (float) Math.sin(Math.toRadians(-angle)),
            0.004f * (float) Math.cos(Math.toRadians(angle))), bullet.getPosition(), true);
        sprite.setRotation((float) Math.toDegrees(bullet.getAngle()));

        shootingMap.get(entity).lastBulletTime = Game.currentTimeMillis;

        EntityManager.getInstance().add(eBullet);
      }
    }
  }
}
