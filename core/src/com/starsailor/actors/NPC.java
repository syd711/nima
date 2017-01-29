package com.starsailor.actors;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.math.Vector2;
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

  private Behaviours behaviour;

  private Route route;
  private NPC guardedNPC;


  /**
   * Constructor used for ships that have been spawned from a route point
   * @param shipProfile
   * @param route
   * @param state
   */
  public NPC(ShipProfile shipProfile, Route route, State state, Behaviours behaviour) {
    super(shipProfile, state);
    this.route = route;
    this.behaviour = behaviour;
    routingComponent = ComponentFactory.addRoutingComponent(this, route);
  }

  /**
   * Constructor used for ships that guard a routing ship
   * @param shipProfile
   * @param guardedNPC
   * @param state
   */
  public NPC(ShipProfile shipProfile, NPC guardedNPC, State state, Behaviours behaviour, Vector2 position) {
    super(shipProfile, state);
    this.behaviour = behaviour;
    this.guardedNPC = guardedNPC;
    this.positionComponent.setPosition(position);
  }


  @Override
  protected void createComponents(ShipProfile profile, State state) {
    super.createComponents(profile, state);

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
