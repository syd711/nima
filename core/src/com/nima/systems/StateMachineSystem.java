package com.nima.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.nima.components.StatefulComponent;

/**
 * Used to update state machines.
 */
public class StateMachineSystem extends IteratingSystem {
  public StateMachineSystem() {
    super(Family.all(StatefulComponent.class).get());
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    StatefulComponent component = entity.getComponent(StatefulComponent.class);
    if(component.stateMachine != null) {
      component.stateMachine.update();
    }
  }
}
