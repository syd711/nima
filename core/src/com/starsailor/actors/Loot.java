package com.starsailor.actors;

import com.badlogic.gdx.math.Vector2;
import com.starsailor.components.ComponentFactory;
import com.starsailor.managers.EntityManager;
import com.starsailor.util.box2d.BodyGenerator;

/**
 * Created for destroyed ships
 */
public class Loot extends GameEntity {

  public Loot(Ship ship) {
    createComponents(ship);
  }

  private void createComponents(Ship ship) {
    Vector2 position = ship.positionComponent.getPosition();
    ComponentFactory.addBodyComponent(this, BodyGenerator.createLootBody(position));
    ComponentFactory.addLootComponent(this).setPosition(position);
    EntityManager.getInstance().add(this);
  }

}
