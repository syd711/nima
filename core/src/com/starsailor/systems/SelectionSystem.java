package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.NPC;
import com.starsailor.components.SelectionComponent;
import com.starsailor.components.SpriteComponent;
import com.starsailor.managers.Textures;

/**
 * Handles the selection and the changed selection rendering
 */
public class SelectionSystem extends PauseableIteratingSystem {

  public SelectionSystem() {
    super(Family.all(SelectionComponent.class).get());
  }

  @Override
  public void process(Entity entity, float deltaTime) {
    if(!(entity instanceof NPC)) {
      return;
    }

    NPC npc = (NPC) entity;
    SpriteComponent spriteComponent = npc.spriteComponent;

    //update selection sprite
    if(npc.selectionComponent.isSelected()) {
      //update selection sprite
      Vector2 center = npc.getCenter();
      spriteComponent.getSprite(Textures.SELECTION).setPosition(center, true);
    }
  }

}
