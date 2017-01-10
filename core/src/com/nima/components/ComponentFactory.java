package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nima.Game;
import com.nima.actors.BodyEntity;
import com.nima.actors.Route;
import com.nima.actors.Spine;
import com.nima.data.RouteProfile;
import com.nima.render.MapConstants;
import com.nima.util.*;

/**
 * All component creations should be here.
 */
public class ComponentFactory {
  public static PooledEngine engine;

  private static <T extends Component> T createComponent (Class<T> componentType) {
    return engine.createComponent(componentType);
  }

  public static MapObjectComponent addMapObjectComponent(Entity entity, MapObject mapObject) {
    MapObjectComponent component = createComponent(MapObjectComponent.class);
    component.mapObject = mapObject;
    entity.add(component);
    return component;
  }

  public static BodyComponent addBodyComponent(Entity entity, MapObject mapObject) {
    BodyComponent component = createComponent(BodyComponent.class);
    component.body = (Body) mapObject.getProperties().get(MapConstants.PROPERTY_COLLISION_COMPONENT);
    entity.add(component);
    return component;
  }

  public static BodyComponent addBodyComponent(BodyEntity entity, String name, short filterCategory, Vector2 position, Sprite sprite) {
    BodyComponent component = createComponent(BodyComponent.class);
    component.body = BodyGenerator.generateBody(entity, sprite, Gdx.files.internal(Resources.SPRITE_BODIES + name + ".json"), filterCategory);
    component.body.setTransform(Box2dUtil.toBox2Vector(position), component.body.getAngle());
    component.body.setUserData(entity);
    entity.add(component);
    return component;
  }

  public static BodyComponent addBodyComponent(Spine spine) {
    BodyComponent component = createComponent(BodyComponent.class);
    component.body = Box2dUtil.createSpineBody(Game.world, spine);
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

  public static SpeedComponent addSpeedComponent(Spine spine) {
    SpeedComponent component = createComponent(SpeedComponent.class);
    component.init(Settings.MAX_ACTOR_SPEED);
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

  public static RotationComponent addRotationComponent(Spine spine) {
    RotationComponent component = createComponent(RotationComponent.class);
    component.spine = spine;
    spine.add(component);
    return component;
  }

  public static MovementComponent addMovementComponent(Spine spine) {
    MovementComponent component = createComponent(MovementComponent.class);
    component.setSpine(spine);
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

  public static ShootingComponent addShootableComponent(Entity entity) {
    ShootingComponent component = createComponent(ShootingComponent.class);
    entity.add(component);
    return component;
  }

  public static RouteComponent addRouteComponent(Route route, RouteProfile routeProfile) {
    RouteComponent component = createComponent(RouteComponent.class);
    component.route = routeProfile;
    route.add(component);
    return component;
  }
}