package com.starsailor.actors;

import com.badlogic.gdx.math.Vector2;
import com.starsailor.components.BodyComponent;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.MarkerComponent;
import com.starsailor.components.SteerableComponent;
import com.starsailor.managers.EntityManager;
import com.starsailor.model.SteeringData;
import com.starsailor.util.box2d.BodyGenerator;

/**
 * The click target the player is moving to.
 */
public class ClickTarget extends GameEntity {
  private static ClickTarget instance = new ClickTarget();

  public static ClickTarget getInstance() {
    return instance;
  }

  private BodyComponent bodyComponent;
  private SteerableComponent steerableComponent;
  private MarkerComponent markerComponent;

  public ClickTarget() {
    bodyComponent = ComponentFactory.addBodyComponent(this, BodyGenerator.createClickBody(new Vector2()));
    markerComponent = ComponentFactory.addMarkerComponent(this);
    steerableComponent = ComponentFactory.addSteerableComponent(this, bodyComponent.body, new SteeringData());
    ComponentFactory.addPlayerCollisionComponent(this);
    markerComponent.setActive(true);

    EntityManager.getInstance().add(this);
  }

  public void update(Vector2 worldCoordinates) {
    markerComponent.setActive(true);
    bodyComponent.setWorldPosition(worldCoordinates);
    markerComponent.setPosition(this);
  }

  public Vector2 getCenter() {
    return bodyComponent.getWorldPosition();
  }

  public SteerableComponent getSteerableComponent() {
    return steerableComponent;
  }

  public void hide() {
    markerComponent.setActive(false);
  }
}