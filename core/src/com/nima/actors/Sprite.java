package com.nima.actors;

import com.nima.components.SpriteComponent;
import com.nima.managers.EntityManager;

/**
 * Common superclass for Sprite entities
 */
public class Sprite extends BodyEntity {
  public SpriteComponent spriteComponent;

  public Sprite(String resourceLocation) {
    spriteComponent = EntityManager.getInstance().addSpriteComponent(this, resourceLocation);
    positionComponent = EntityManager.getInstance().addPositionComponent(this, true, spriteComponent.sprite.getHeight());
    positionComponent.z = 900;
    bodyComponent = EntityManager.getInstance().addBodyComponent(this, positionComponent, spriteComponent.sprite);
  }
}
