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
import com.nima.components.ShootingComponent;
import com.nima.managers.EntityManager;

/**
 * Used during fighting
 */
public class ShootingSystem extends IteratingSystem {
  private ComponentMapper<ShootingComponent> shootingMap = ComponentMapper.getFor(ShootingComponent.class);

  public ShootingSystem() {
    super(Family.all(ShootingComponent.class).get());
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    int bulletDelayMillis = 1350;
    long lastBulletTime = shootingMap.get(entity).lastBulletTime;

    if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
      if(Game.currentTimeMillis - lastBulletTime > bulletDelayMillis) {
        Bullet bullet = Bullet.newBullet();

        //TODO dont use player here
        Vector2 toTarget = Player.getInstance().getComponent(BodyComponent.class).body.getPosition().sub(bullet.bodyComponent.body.getPosition());
        float angle = (float) Math.atan2(-toTarget.x, toTarget.y);
        angle = (float) Math.toDegrees(angle);

        Body bulletBody = bullet.bodyComponent.body;
        Sprite sprite = bullet.spriteComponent.sprite;
        bulletBody.setTransform(bulletBody.getPosition().x, bulletBody.getPosition().y, (float) Math.toRadians(angle - 90f));
        bulletBody.applyLinearImpulse(new Vector2(0.004f * (float) Math.sin(Math.toRadians(-angle)),
            0.004f * (float) Math.cos(Math.toRadians(angle))), bulletBody.getPosition(), true);
        sprite.setRotation((float) Math.toDegrees(bulletBody.getAngle()));

        shootingMap.get(entity).lastBulletTime = Game.currentTimeMillis;

        EntityManager.getInstance().add(bullet);
      }
    }
  }
}
