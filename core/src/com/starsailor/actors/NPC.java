package com.starsailor.actors;

import com.badlogic.gdx.ai.fsm.State;
import com.starsailor.actors.states.npc.NPCStates;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.RoutingComponent;
import com.starsailor.components.SelectionComponent;
import com.starsailor.components.collision.NPCCollisionComponent;
import com.starsailor.data.ShipProfile;

/**
 * Common superclass for all NPC.
 * We assume that they are instances of Spine.
 */
public class NPC extends Ship implements Selectable {
  public RoutingComponent routingComponent;
  public SelectionComponent selectionComponent;
  public NPCCollisionComponent collisionComponent;

  protected State<NPC> defaultState;

  public NPC(ShipProfile profile, State<NPC> defaultState) {
    super(profile);
    this.shipProfile = profile;
    this.defaultState = defaultState;
  }

  @Override
  public void createComponents(ShipProfile profile) {
    super.createComponents(profile);
    collisionComponent = ComponentFactory.addNPCCollisionComponent(this);
    selectionComponent = ComponentFactory.addSelectionComponent(this);

    getStateMachine().setInitialState(NPCStates.IDLE);
  }

  @Override
  public SelectionComponent getSelectionComponent() {
    return selectionComponent;
  }

  @Override
  public void setSelected(boolean b) {
    selectionComponent.selected = b;
    selectionComponent.setSelected(b);
    spriteComponent.setEnabled(b);
  }

  @Override
  public boolean isSelected() {
    return selectionComponent.selected;
  }
}
