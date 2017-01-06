package com.nima.actors;

import com.nima.components.ComponentFactory;
import com.nima.components.SpriteComponent;

/**
 * Common superclass for Sprite entities
 */
public class Sprite extends BodyEntity {
  public SpriteComponent spriteComponent;

  public Sprite(String resourceLocation) {
    spriteComponent = ComponentFactory.addSpriteComponent(this, resourceLocation);
    positionComponent = ComponentFactory.addPositionComponent(this, true, spriteComponent.sprite.getHeight());
    positionComponent.z = 900;
    bodyComponent = ComponentFactory.addBodyComponent(this, positionComponent, spriteComponent.sprite);
  }
}
