package com.nima.actors;

import com.badlogic.gdx.math.Vector2;
import com.nima.components.ComponentFactory;
import com.nima.components.SpriteComponent;

/**
 * Common superclass for Sprite entities
 */
public class Sprite extends BodyEntity {
  public SpriteComponent spriteComponent;

  public Sprite(String resourceLocation, Vector2 defaultPosition) {
    spriteComponent = ComponentFactory.addSpriteComponent(this, resourceLocation);
    positionComponent = ComponentFactory.addPositionComponent(this);
    positionComponent.setPosition(defaultPosition);
    positionComponent.z = 900;
    bodyComponent = ComponentFactory.addBodyComponent(this, defaultPosition, spriteComponent.sprite);
  }
}
