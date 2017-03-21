package com.starsailor.actors;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.route.Route;
import com.starsailor.actors.states.formation.FormationIdleState;
import com.starsailor.actors.states.formation.FormationRouteState;
import com.starsailor.components.*;
import com.starsailor.model.BodyData;
import com.starsailor.model.SteeringData;
import com.starsailor.util.box2d.BodyGenerator;
import com.starsailor.util.box2d.Box2dUtil;

import java.util.List;

/**
 * Invisible route rabbit to be followed in a formation
 */
public class FormationOwner extends GameEntity implements IFormationOwner<Ship> {
  public static final float FORMATION_DISTANCE = 100;

  public BodyComponent bodyComponent;
  public SteerableComponent steerableComponent;
  public FormationComponent formationComponent;
  public StatefulComponent statefulComponent;
  public RoutingComponent routingComponent;

  private Route route;

  public FormationOwner(Route route, int routePointIndex) {
    this.route = route;
    createComponents(routePointIndex);
  }

  private void createComponents(int routePointIndex) {
    statefulComponent = ComponentFactory.addStatefulComponent(this);
    routingComponent = ComponentFactory.addRoutingComponent(this, route);

    Vector2 origin = routingComponent.getWayPoint(routePointIndex).getBox2dPosition();
    bodyComponent = ComponentFactory.addBodyComponent(this, BodyGenerator.create(getBodyData(), Box2dUtil.toWorldPoint(origin)));
    steerableComponent = ComponentFactory.addSteerableComponent(this, bodyComponent.body, getSteeringData());
    formationComponent = ComponentFactory.addFormationComponent(this, steerableComponent, FORMATION_DISTANCE);

    statefulComponent.stateMachine.setInitialState(new FormationIdleState());
    changeState(new FormationRouteState());
  }

  public void changeState(State<FormationOwner> state) {
    statefulComponent.stateMachine.changeState(state);
  }

  public boolean isInBattleState() {
    List<Ship> members = formationComponent.getMembers();
    for(Ship member : members) {
      if(member.isInBattleState()) {
        return true;
      }
    }

    return false;
  }

  @Override
  public List<Ship> getMembers() {
    return formationComponent.getMembers();
  }

  @Override
  public void addMember(Ship ship) {
    formationComponent.addMember(ship);
    ship.setFormationOwner(this);
  }

  @Override
  public void removeMember(Ship member) {
    formationComponent.removeMember(member);
  }

  @Override
  public float getMaxMemberDistance() {
    float distance = 0;
    for(Ship ship : getMembers()) {
      float distanceTo = ship.getDistanceTo(this);
      if(distanceTo > distance) {
        distance = distanceTo;
      }
    }
    return distance;
  }

  //-------------------- Helper ---------------------------------------------------

  /**
   * The dummy body hard coded
   *
   * @return
   */
  private BodyData getBodyData() {
    BodyData bodyData = new BodyData();
    bodyData.setRadius(30);
    bodyData.setAngularDamping(3);
    bodyData.setLinearDamping(12);
    bodyData.setSensor(true);
    bodyData.setDensity(1); //make more here since the object is a small dummy
    bodyData.setHeight(10);
    bodyData.setWidth(10);
    return bodyData;
  }

  /**
   * The dummy steering data hard coded
   *
   * @return
   */
  private SteeringData getSteeringData() {
    SteeringData steeringData = new SteeringData();
    steeringData.setBoundingRadius(200);
    steeringData.setMaxLinearAcceleration(0.6f);
    steeringData.setMaxLinearSpeed(0.4f);
    steeringData.setMaxAngularAcceleration(4f);
    steeringData.setMaxAngularSpeed(4f);
    return steeringData;
  }

  @Override
  public String toString() {
    return "Formation Owner on " + route;
  }

}
