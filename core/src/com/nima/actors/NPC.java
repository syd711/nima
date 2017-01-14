package com.nima.actors;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.nima.actors.states.NPCState;
import com.nima.components.ComponentFactory;
import com.nima.components.RoutingComponent;
import com.nima.data.ShipProfile;

import static com.nima.util.Settings.MPP;

/**
 * Common superclass for all NPC.
 * We assume that they are instances of Spine.
 */
public class NPC extends Ship {
  public RoutingComponent routingComponent;

//  public NPC(ShipProfile shipProfile) {
//    super(shipProfile);
//
//    Vector2 screenCenter = GraphicsUtil.getScreenCenter(getHeight());
//    positionComponent.x = screenCenter.x + 360;
//    positionComponent.y = screenCenter.y + 60;
//
//    bodyComponent.body.setTransform(positionComponent.x * MPP, positionComponent.y * MPP, 0);
//
//    statefulComponent.stateMachine = new DefaultStateMachine<>(this, NPCState.SLEEP);
//  }


  public NPC(ShipProfile shipProfile, Route route) {
    super(shipProfile);
    routingComponent = ComponentFactory.addRoutingComponent(this, route);
    bodyComponent.body.setTransform(routingComponent.target.position.x * MPP, routingComponent.target.position.y * MPP, 0);
    statefulComponent.stateMachine = new DefaultStateMachine<>(this, NPCState.IDLE);
    statefulComponent.stateMachine.changeState(NPCState.ROUTE);
  }
}
