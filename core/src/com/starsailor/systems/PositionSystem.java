package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Spine;
import com.starsailor.components.BodyComponent;
import com.starsailor.components.PositionComponent;

public class PositionSystem extends AbstractIteratingSystem {
  public PositionSystem() {
    super(Family.all(PositionComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    //check all entities that have a body and update their position and rotation according to their body
    if(entity instanceof Spine){

      //TODO
      if(entity instanceof NPC && ((NPC)entity).selectionComponent.selected) {
        Vector2 center = ((NPC)entity).getCenter();
        ((NPC)entity).spriteComponent.setSelectionMarkerAt(center);
      }
    }
  }
}