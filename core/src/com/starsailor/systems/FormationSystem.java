package com.starsailor.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.starsailor.components.FormationComponent;

public class FormationSystem extends AbstractIteratingSystem {
  private ComponentMapper<FormationComponent> steerableMap = ComponentMapper.getFor(FormationComponent.class);

  public FormationSystem() {
    super(Family.all(FormationComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    FormationComponent steerableComponent = steerableMap.get(entity);
    steerableComponent.updateFormation();
  }
}