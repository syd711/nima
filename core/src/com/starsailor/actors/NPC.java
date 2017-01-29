package com.starsailor.actors;

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

  protected Behaviours behaviour;

  public NPC(ShipProfile profile, Behaviours behaviour) {
    super(profile);
    this.shipProfile = profile;
    this.behaviour = behaviour;
  }

  @Override
  public void createComponents(ShipProfile profile) {
    super.createComponents(profile);
    collisionComponent = ComponentFactory.addNPCCollisionComponent(this);
    selectionComponent = ComponentFactory.addSelectionComponent(this);
  }

  public boolean isAggressive() {
    return behaviour != null && behaviour.equals(Behaviours.AGGRESSIVE);
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
