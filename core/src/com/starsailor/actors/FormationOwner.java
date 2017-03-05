package com.starsailor.actors;

import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.route.Route;
import com.starsailor.actors.route.RoutePoint;
import com.starsailor.components.*;
import com.starsailor.model.BodyData;
import com.starsailor.model.SteeringData;
import com.starsailor.util.Settings;
import com.starsailor.util.box2d.BodyGenerator;

import java.util.List;

/**
 * Invisible route rabbit to be followed in a formation
 */
public class FormationOwner extends GameEntity implements IFormationOwner<Ship> {
  public static final float FORMATION_DISTANCE = 100;

  private PositionComponent positionComponent;
  private BodyComponent bodyComponent;
  private SteerableComponent steerableComponent;
  private FormationComponent formationComponent;
  private RoutingComponent routingComponent;

  private Route route;

  public FormationOwner(Route route, RoutePoint startPoint) {
    this.route = route;
  }

  public void createComponents(Vector2 start) {
    positionComponent = ComponentFactory.addPositionComponent(this, false, getBodyData().getHeight() * Settings.PPM);
    bodyComponent = ComponentFactory.addBodyComponent(this, BodyGenerator.create(getBodyData(), start));
    steerableComponent = ComponentFactory.addSteerableComponent(this, bodyComponent.body, getSteeringData());
    formationComponent = ComponentFactory.addFormationComponent(this, steerableComponent, FORMATION_DISTANCE);

    if(this.route != null) {
      routingComponent = ComponentFactory.addRoutingComponent(this, route);
    }
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
    bodyData.setRadius(50);
    bodyData.setAngularDamping(3);
    bodyData.setLinearDamping(6);
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
    steeringData.setMaxLinearAcceleration(2.0f);
    steeringData.setMaxLinearSpeed(2f);
    steeringData.setMaxAngularAcceleration(4f);
    steeringData.setMaxAngularSpeed(4f);
    return steeringData;
  }
}
