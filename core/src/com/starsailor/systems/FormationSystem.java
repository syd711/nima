package com.starsailor.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Family;
import com.starsailor.actors.GameEntity;
import com.starsailor.components.FormationComponent;

public class FormationSystem extends PauseableIteratingSystem {
  private ComponentMapper<FormationComponent> steerableMap = ComponentMapper.getFor(FormationComponent.class);

  public FormationSystem() {
    super(Family.all(FormationComponent.class).get());
  }

  public void process(GameEntity entity, float deltaTime) {
    FormationComponent steerableComponent = steerableMap.get(entity);
    steerableComponent.updateFormation();
  }
}