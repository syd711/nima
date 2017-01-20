package com.starsailor.actors;

import com.badlogic.gdx.ai.fsm.State;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.RoutingComponent;
import com.starsailor.components.SelectionComponent;
import com.starsailor.components.SpriteComponent;
import com.starsailor.components.collision.NPCCollisionComponent;
import com.starsailor.data.ShipProfile;
import com.starsailor.render.converters.MapConstants;

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

  public boolean toggleSelection() {
    boolean selected = selectionComponent.toggleSelection();
    if(selected) {
      spriteComponent = ComponentFactory.addSpriteComponent(this, "selection");
    }
    else {
      this.remove(SpriteComponent.class);
    }
    return selected;
  }

  public String getBehaviour() {
    return route.routeComponent.behaviour;
  }
}
