package com.nima.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector2;
import com.nima.actors.NPC;
import com.nima.components.RoutingComponent;

import java.util.Iterator;

public class RoutingSystem extends AbstractIteratingSystem {
  public RoutingSystem() {
    super(Family.all(RoutingComponent.class).get());
  }

  public void process(Entity entity, float deltaTime) {
    NPC npc = (NPC) entity;
    RoutingComponent component = entity.getComponent(RoutingComponent.class);
    if(component.target == null) {
      Iterator<Vector2> iterator = component.targets.iterator();
      Vector2 target = iterator.next();
//      component.target = target;
//      npc.moveTo(target.x, target.y, 4f);
    }
  }
}