package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ai.fma.Formation;
import com.badlogic.gdx.ai.fma.FormationMember;
import com.badlogic.gdx.ai.fma.SoftRoleSlotAssignmentStrategy;
import com.badlogic.gdx.ai.fma.patterns.DefensiveCircleFormationPattern;
import com.badlogic.gdx.ai.fsm.StackStateMachine;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.starsailor.Game;
import com.starsailor.actors.FormationOwner;
import com.starsailor.actors.Fraction;
import com.starsailor.actors.Ship;
import com.starsailor.actors.route.Route;
import com.starsailor.components.collision.*;
import com.starsailor.managers.ParticleManager;
import com.starsailor.managers.ResourceManager;
import com.starsailor.messaging.Messages;
import com.starsailor.model.BodyData;
import com.starsailor.model.ShieldData;
import com.starsailor.model.SpineData;
import com.starsailor.model.WeaponData;
import com.starsailor.render.converters.MapConstants;
import com.starsailor.util.GraphicsUtil;
import com.starsailor.util.box2d.BodyGenerator;
import com.starsailor.util.box2d.Box2dUtil;

import java.util.List;

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

  public static BodyComponent addBulletBodyComponent(Entity entity, Vector2 position, WeaponData weaponData, boolean friendly) {
    BodyComponent component = createComponent(BodyComponent.class);
    component.body = BodyGenerator.createBulletBody(position, weaponData, friendly);
    component.body.setTransform(Box2dUtil.toBox2Vector(position), component.body.getAngle());
    component.body.setUserData(entity);
    entity.add(component);
    return component;
  }

  public static ShipBodyComponent addShipBodyComponent(Ship ship, BodyData bodyData, Vector2 position) {
    ShipBodyComponent component = createComponent(ShipBodyComponent.class);
    component.body = BodyGenerator.createSpineBody(Game.world, ship, bodyData);
    component.body.setUserData(ship);

    if(position != null) {
      Vector2 box2dPos = Box2dUtil.toBox2Vector(position);
      component.body.setTransform(box2dPos, component.body.getAngle());
    }

    ship.add(component);
    return component;
  }

  public static ScalingComponent addScalingComponent(Entity entity) {
    ScalingComponent component = createComponent(ScalingComponent.class);
    component.init(1f);
    entity.add(component);
    return component;
  }

  public static LightComponent addLightComponent(Entity entity) {
    LightComponent component = createComponent(LightComponent.class);
    entity.add(component);
    return component;
  }

  public static SpineShipComponent addSpineShipComponent(Entity entity, SpineData spineData) {
    SpineShipComponent component = new SpineShipComponent(spineData);
    entity.add(component);
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

  public static ShootingComponent addShootableComponent(Entity entity, com.starsailor.model.ShipData profile) {
    ShootingComponent component = createComponent(ShootingComponent.class);
    component.setWeaponDatas(profile.getStatusData().getWeaponDatas());
    entity.add(component);
    return component;
  }

  public static RouteComponent addRouteComponent(Route route) {
    RouteComponent component = createComponent(RouteComponent.class);
    route.add(component);
    return component;
  }

  public static SteerableComponent addSteerableComponent(Entity entity, Body body, com.starsailor.model.SteeringData steeringData) {
    SteerableComponent component = createComponent(SteerableComponent.class);
    component.init(body, steeringData, true);
    entity.add(component);
    return component;
  }

  public static StatefulComponent addStatefulComponent(Entity entity) {
    StatefulComponent component = createComponent(StatefulComponent.class);
    component.stateMachine = new StackStateMachine<>(entity);
    MessageManager.getInstance().addListener(component.stateMachine, Messages.ATTACKED);
    MessageManager.getInstance().addListener(component.stateMachine, Messages.ATTACK);
    entity.add(component);
    return component;
  }

  public static RoutingComponent addRoutingComponent(Entity entity, Route route) {
    RoutingComponent component = createComponent(RoutingComponent.class);
    component.setTargets(route.routeComponent.getRoutePoints());
    entity.add(component);
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

  public static SelectionComponent addSelectionComponent(Entity entity) {
    SelectionComponent component = createComponent(SelectionComponent.class);
    entity.add(component);
    return component;
  }

  public static BulletDamageComponent addBulletDamageComponent(Entity entity, com.starsailor.model.WeaponData weaponData) {
    BulletDamageComponent component = createComponent(BulletDamageComponent.class);
    component.damage = weaponData.getDamage();
    entity.add(component);
    return component;
  }

  public static ParticleComponent addParticleComponent(Entity entity, String particleEffect) {
    ParticleComponent component = createComponent(ParticleComponent.class);
    component.effect = ParticleManager.getInstance().getEffect(particleEffect);
    component.particleEffect = particleEffect;
    entity.add(component);
    return component;
  }

  public static AnimationComponent addAnimationComponent(Entity entity, String texturePrefix) {
    AnimationComponent component = createComponent(AnimationComponent.class);
    List<Texture> textures = ResourceManager.getInstance().getTextureAssets(texturePrefix);
    component.textures.addAll(textures);
    entity.add(component);
    return component;
  }

  public static SpineMarkerComponent addSpineMarkerComponent(Entity entity) {
    SpineMarkerComponent component = createComponent(SpineMarkerComponent.class);
    entity.add(component);
    return component;
  }

  public static SpineBulletComponent addSpineBulletComponent(Entity entity, SpineData spineData) {
    SpineBulletComponent component = new SpineBulletComponent(spineData);
    entity.add(component);
    return component;
  }

  public static ShieldSpineComponent addSpineShieldComponent(Entity entity, SpineData spineData) {
    ShieldSpineComponent shieldSpineComponent = new ShieldSpineComponent(spineData);
    shieldSpineComponent.setEnabled(false);
    entity.add(shieldSpineComponent);
    return shieldSpineComponent;
  }

  public static ShieldBodyComponent addShieldBodyComponent(Entity entity) {
    ShieldBodyComponent component = createComponent(ShieldBodyComponent.class);
    entity.add(component);
    return component;
  }

  public static ShieldStatusComponent addShieldComponent(Entity entity, ShieldData shieldData) {
    ShieldStatusComponent component = createComponent(ShieldStatusComponent.class);
    if(shieldData != null) {
      component.health = shieldData.getHealth();
      component.maxHealth = shieldData.getHealth();
      component.damageAbsorptionFactor = shieldData.getDamageAbsorptionFactor();
    }
    entity.add(component);
    return component;
  }

  public static FormationComponent addFormationComponent(FormationOwner formationOwner, SteerableComponent formationOwnerSteering, float distance) {
    FormationComponent component = createComponent(FormationComponent.class);
    SoftRoleSlotAssignmentStrategy.SlotCostProvider<Vector2> slotCostProvider = new SoftRoleSlotAssignmentStrategy.SlotCostProvider<Vector2>() {
      @Override
      public float getCost (FormationMember<Vector2> member, int slotNumber) {
        Ship ship = (Ship)member;
        float cost = 10000f * 2 * component.getFormation().getSlotAssignmentCount();
        Location<Vector2> slotTarget = component.getFormation().getSlotAssignmentAt(slotNumber).member.getTargetLocation();
        Body body = ship.shipBodyComponent.body;
        return cost + body.getPosition().dst(slotTarget.getPosition());
      }
    };
    SoftRoleSlotAssignmentStrategy slotAssignmentStrategy = new SoftRoleSlotAssignmentStrategy<>(slotCostProvider);
    DefensiveCircleFormationPattern<Vector2> defensiveCirclePattern = new DefensiveCircleFormationPattern<>(distance * MPP);

    Formation<Vector2> formation = new Formation<>(formationOwnerSteering, defensiveCirclePattern, slotAssignmentStrategy);
    formation.setMotionModerator(new DefaultFormationModerator(formationOwner));

    component.setFormation(formation);
    formationOwner.add(component);
    return component;
  }

  public static FractionComponent createFractionComponent(Entity entity, Fraction fraction) {
    FractionComponent component = createComponent(FractionComponent.class);
    component.fraction = fraction;
    entity.add(component);
    return component;
  }

  public static HealthComponent addHealthComponent(Entity entity, com.starsailor.model.ShipData shipData) {
    HealthComponent component = createComponent(HealthComponent.class);
    component.maxHealth = shipData.getStatusData().getHealth();
    component.health = shipData.getStatusData().getHealth();
    entity.add(component);
    return component;
  }
}
