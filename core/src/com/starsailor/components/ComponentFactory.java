package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.starsailor.Game;
import com.starsailor.actors.Route;
import com.starsailor.actors.Spine;
import com.starsailor.components.collision.*;
import com.starsailor.data.ShipProfile;
import com.starsailor.data.SteeringData;
import com.starsailor.render.converters.MapConstants;
import com.starsailor.util.BodyGenerator;
import com.starsailor.util.Box2dUtil;
import com.starsailor.util.GraphicsUtil;
import com.starsailor.util.Resources;

import static com.starsailor.util.Settings.MPP;

/**
 * All component creations should be here.
 */
public class ComponentFactory {
  public static PooledEngine engine;

  private static <T extends Component> T createComponent(Class<T> componentType) {
    return engine.createComponent(componentType);
  }

  public static MapObjectComponent addMapObjectComponent(Entity entity, MapObject mapObject) {
    MapObjectComponent component = createComponent(MapObjectComponent.class);
    component.mapObject = mapObject;
    entity.add(component);
    return component;
  }

  public static BodyComponent addBodyComponent(Entity entity, Body body) {
    BodyComponent component = createComponent(BodyComponent.class);
    body.setUserData(entity);
    component.body = body;
    entity.add(component);
    return component;
  }


  public static BodyComponent addBodyComponent(Entity entity, MapObject mapObject) {
    BodyComponent component = createComponent(BodyComponent.class);
    component.body = (Body) mapObject.getProperties().get(MapConstants.PROPERTY_BOX2D_BODY);
    entity.add(component);
    return component;
  }

  public static BodyComponent addBulletBodyComponent(Entity entity, Vector2 position, boolean friendly) {
    BodyComponent component = createComponent(BodyComponent.class);
    component.body = BodyGenerator.createBulletBody(position, friendly);
    component.body.setTransform(Box2dUtil.toBox2Vector(position), component.body.getAngle());
    component.body.setUserData(entity);
    entity.add(component);
    return component;
  }

  public static BodyComponent addBodyComponent(Spine spine) {
    BodyComponent component = createComponent(BodyComponent.class);
    component.body = BodyGenerator.createSpineBody(Game.world, spine);
    component.body.setUserData(spine);
    spine.add(component);
    return component;
  }

  public static ScalingComponent addScalingComponent(Spine spine) {
    ScalingComponent component = createComponent(ScalingComponent.class);
    component.init(1f);
    spine.add(component);
    return component;
  }

  public static SpeedComponent addSpeedComponent(Spine spine, ShipProfile profile) {
    SpeedComponent component = createComponent(SpeedComponent.class);
    component.init(0, 0, profile.maxSpeed, profile.increaseSpeed, profile.decreaseSpeed);
    spine.add(component);
    return component;
  }

  public static LightComponent addLightComponent(Entity entity) {
    LightComponent component = createComponent(LightComponent.class);
    entity.add(component);
    return component;
  }

  public static SpineComponent addSpineComponent(Spine spine) {
    SpineComponent component = createComponent(SpineComponent.class);
    spine.add(component);
    return component;
  }

  public static RotationComponent addRotationComponent(Spine spine, ShipProfile profile) {
    RotationComponent component = createComponent(RotationComponent.class);
    component.spine = spine;
    component.rotationSpeed = profile.rotationSpeed;
    spine.add(component);
    return component;
  }

  public static PositionComponent addPositionComponent(Entity entity) {
    PositionComponent component = createComponent(PositionComponent.class);
    entity.add(component);
    return component;
  }

  public static PositionComponent addPositionComponent(Entity entity, boolean initCentered, float heightOffset) {
    PositionComponent component = addPositionComponent(entity);
    if(initCentered) {
      Vector2 screenCenter = GraphicsUtil.getScreenCenter(heightOffset);
      component.x = screenCenter.x;
      component.y = screenCenter.y;
    }
    return component;
  }

  public static SpriteComponent addSpriteComponent(Entity entity, String resourceLocation) {
    SpriteComponent component = createComponent(SpriteComponent.class);
    component.setTextures(new Texture(Resources.SPRITE_TEXTURES + resourceLocation + ".png"));
    entity.add(component);
    return component;
  }

  public static BulletDamageComponent addBulletDamageComponent(Entity entity, int damage) {
    BulletDamageComponent component = createComponent(BulletDamageComponent.class);
    component.damage = damage;
    entity.add(component);
    return component;
  }

  public static ShootingComponent addShootableComponent(Entity entity, ShipProfile profile) {
    ShootingComponent component = createComponent(ShootingComponent.class);
    component.setWeaponProfiles(profile.weaponProfiles);
    entity.add(component);
    return component;
  }

  public static RouteComponent addRouteComponent(Route route, String name) {
    RouteComponent component = createComponent(RouteComponent.class);
    component.name = name;
    route.add(component);
    return component;
  }

  public static SteerableComponent addSteerableComponent(Entity entity, Body body, SteeringData steeringData) {
    SteerableComponent component = createComponent(SteerableComponent.class);
    component.init(body, steeringData);
    entity.add(component);
    return component;
  }

  public static StatefulComponent addStatefulComponent(Entity entity, State state) {
    StatefulComponent component = createComponent(StatefulComponent.class);
    component.stateMachine = new DefaultStateMachine<>(entity, state);
    entity.add(component);
    return component;
  }

  public static StatefulComponent addStatefulComponent(Entity entity) {
    StatefulComponent component = createComponent(StatefulComponent.class);
    entity.add(component);
    return component;
  }

  public static RoutingComponent addRoutingComponent(Entity entity, Route route) {
    RoutingComponent component = createComponent(RoutingComponent.class);
    component.target = route.routeComponent.spawnPoint;
    component.targets = route.routeComponent.routeCoordinates;
    entity.add(component);

    //apply body position to route too
    BodyComponent bodyComponent = entity.getComponent(BodyComponent.class);
    bodyComponent.body.setTransform(component.target.position.x * MPP, component.target.position.y * MPP, 0);

    return component;
  }

  public static LocationCollisionComponent addLocationCollisionComponent(Entity entity) {
    LocationCollisionComponent component = createComponent(LocationCollisionComponent.class);
    entity.add(component);
    return component;
  }

  public static BulletCollisionComponent addBulletCollisionComponent(Entity entity) {
    BulletCollisionComponent component = createComponent(BulletCollisionComponent.class);
    entity.add(component);
    return component;
  }

  public static NPCCollisionComponent addNPCCollisionComponent(Entity entity) {
    NPCCollisionComponent component = createComponent(NPCCollisionComponent.class);
    entity.add(component);
    return component;
  }

  public static PlayerCollisionComponent addPlayerCollisionComponent(Entity entity) {
    PlayerCollisionComponent component = createComponent(PlayerCollisionComponent.class);
    entity.add(component);
    return component;
  }

  public static RoutePointCollisionComponent addRoutePointCollisionComponent(Entity entity) {
    RoutePointCollisionComponent component = createComponent(RoutePointCollisionComponent.class);
    entity.add(component);
    return component;
  }

  public static DamageComponent addDamageComponent(Entity entity, ShipProfile profile) {
    DamageComponent component = createComponent(DamageComponent.class);
    component.health = profile.health;
    entity.add(component);
    return component;
  }

  public static SelectionComponent addSelectionComponent(Entity entity) {
    SelectionComponent component = createComponent(SelectionComponent.class);
    entity.add(component);
    return component;
  }

  public static PlayerTargetCollisionComponent addPlayerTargetCollisionComponent(Entity entity) {
    PlayerTargetCollisionComponent component = createComponent(PlayerTargetCollisionComponent.class);
    entity.add(component);
    return component;
  }

}
