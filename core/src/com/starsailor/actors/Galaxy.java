package com.starsailor.actors;

import com.badlogic.gdx.math.Vector2;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.BodyGalaxyComponent;
import com.starsailor.util.box2d.Box2dUtil;

/**
 * Encapsulates the box2d body that restricts the user to this current world.
 */
public class Galaxy extends GameEntity {

  private static Galaxy instance;
  private BodyGalaxyComponent bodyGalaxyComponent;
  private String name;

  public static Galaxy getInstance() {
    return instance;
  }

  public Galaxy() {
    instance = this;
  }

  public void load(String name) {
    this.name = name;
    if(bodyGalaxyComponent != null) {
      bodyGalaxyComponent.destroy();
    }
    bodyGalaxyComponent = ComponentFactory.createGalaxyBodyComponent(this);
  }

  public Vector2 getCenter() {
    return Box2dUtil.toWorldPoint(bodyGalaxyComponent.body.getPosition());
  }

  public String getName() {
    return name;
  }
}
