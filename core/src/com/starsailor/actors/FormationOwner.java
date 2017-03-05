package com.starsailor.actors;

import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.route.Route;
import com.starsailor.components.*;
import com.starsailor.managers.SteeringManager;
import com.starsailor.model.BodyData;
import com.starsailor.model.SteeringData;
import com.starsailor.util.Settings;
import com.starsailor.util.box2d.BodyGenerator;
import com.starsailor.util.box2d.Box2dUtil;

import java.util.List;

/**
 * Invisible route rabbit to be followed in a formation
 */
public class FormationOwner extends GameEntity implements IFormationOwner<Ship> {
  public static final float FORMATION_DISTANCE = 100;

  private BodyComponent bodyComponent;
  private SteerableComponent steerableComponent;
  private FormationComponent formationComponent;
  private RoutingComponent routingComponent;

  private Route route;

  public FormationOwner(Route route) {
    this.route = route;
    createComponents();
  }

  private void createComponents() {
    routingComponent = ComponentFactory.addRoutingComponent(this, route);

    Vector2 origin = routingComponent.getWayPoints(null).get(0);
    bodyComponent = ComponentFactory.addBodyComponent(this, BodyGenerator.create(getBodyData(), Box2dUtil.toWorldPoint(origin)));
    steerableComponent = ComponentFactory.addSteerableComponent(this, bodyComponent.body, getSteeringData());
    formationComponent = ComponentFactory.addFormationComponent(this, steerableComponent, FORMATION_DISTANCE);

    SteeringManager.setRouteRabbitSteering(steerableComponent, routingComponent, origin);
  }

  @Override
  public List<Ship> getMembers() {
    return formationComponent.getMembers();
  }

  @Override
  public void addMember(Ship ship) {
    formationComponent.addMember(ship);
  }

  @Override
  public void removeMember(Ship member) {
    formationComponent.removeMember(member);
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
    bodyData.setDensity(5); //make more here since the object is a small dummy
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
    steeringData.setMaxLinearAcceleration(0.8f);
    steeringData.setMaxLinearSpeed(0.6f);
    steeringData.setMaxAngularAcceleration(4f);
    steeringData.setMaxAngularSpeed(4f);
    return steeringData;
  }
}
