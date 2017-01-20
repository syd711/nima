package com.nima.actors;

import com.badlogic.gdx.ai.fsm.State;
import com.nima.components.ComponentFactory;
import com.nima.components.RoutingComponent;
import com.nima.components.SelectionComponent;
import com.nima.components.SpriteComponent;
import com.nima.components.collision.NPCCollisionComponent;
import com.nima.data.ShipProfile;
import com.nima.render.converters.MapConstants;

/**
 * Common superclass for all NPC.
 * We assume that they are instances of Spine.
 */
public class NPC extends Ship {
  public RoutingComponent routingComponent;
  public SelectionComponent selectionComponent;
  public NPCCollisionComponent collisionComponent;

  private Route route;

  public NPC(ShipProfile shipProfile, Route route, State state) {
    super(shipProfile, state);
    this.route = route;

    routingComponent = ComponentFactory.addRoutingComponent(this, route);
    collisionComponent = ComponentFactory.addNPCCollisionComponent(this);
    selectionComponent = ComponentFactory.addSelectionComponent(this);
  }

  public boolean isAggressive() {
    return getBehaviour() != null && getBehaviour().equalsIgnoreCase(MapConstants.BEHAVIOUR_AGGRESSIVE);
  }

  public void toggleSelection() {
    boolean selected = selectionComponent.toggleSelection();
    if(selected) {
      spriteComponent = ComponentFactory.addSpriteComponent(this, "selection");
    }
    else {
      this.remove(SpriteComponent.class);
    }
  }

  public String getBehaviour() {
    return route.routeComponent.behaviour;
  }
}
