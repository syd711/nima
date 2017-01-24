package com.starsailor.actors;

import com.badlogic.gdx.ai.fsm.State;
import com.starsailor.actors.states.NPCStates;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.RoutingComponent;
import com.starsailor.components.SelectionComponent;
import com.starsailor.components.collision.NPCCollisionComponent;
import com.starsailor.data.ShipProfile;
import com.starsailor.render.converters.MapConstants;

/**
 * Common superclass for all NPC.
 * We assume that they are instances of Spine.
 */
public class NPC extends Ship implements Selectable {
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

  public String getBehaviour() {
    return route.routeComponent.behaviour;
  }

  @Override
  public SelectionComponent getSelectionComponent() {
    return selectionComponent;
  }

  @Override
  public void setSelected(boolean b) {
    selectionComponent.selected = b;
    if(selectionComponent.selected) {
      getStateMachine().changeState(NPCStates.SELECT);
    }
    else {
      getStateMachine().changeState(NPCStates.DESELECT);
    }
  }
}
