package com.nima.actors;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.nima.components.BodyComponent;
import com.nima.components.ComponentFactory;
import com.nima.components.PositionComponent;
import com.nima.components.SpriteComponent;

/**
 * Common superclass for Sprite entities
 */
public class Sprite extends Entity {
  public SpriteComponent spriteComponent;
  public PositionComponent positionComponent;
  public BodyComponent bodyComponent;

  /**
   * The name of the sprite determines the texture and the body definition!
   * @param name the name of the sprite, used to locate the json and png
   * @param defaultPosition the default position of the sprite
   */
  public Sprite(String name, Vector2 defaultPosition) {
    spriteComponent = ComponentFactory.addSpriteComponent(this, name);
    positionComponent = ComponentFactory.addPositionComponent(this);
    positionComponent.setPosition(defaultPosition);
    positionComponent.z = 900;
  }
}
