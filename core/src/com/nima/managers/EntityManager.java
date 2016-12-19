package com.nima.managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.nima.actors.Updateable;
import com.nima.components.DimensionComponent;
import com.nima.components.PositionComponent;
import com.nima.components.SpineComponent;
import com.nima.render.TiledMultiMapRenderer;
import com.nima.actors.Camera;
import com.nima.systems.PositionSystem;
import com.nima.systems.SpineRenderSystem;
import com.nima.util.Settings;

import java.util.ArrayList;
import java.util.List;

/**
 * Central Ashley initialization of entity systems.
 */
public class EntityManager {
  private final PooledEngine engine;
  private static Entity player;
  private static List<Updateable> updateables = new ArrayList();

  public EntityManager(PooledEngine engine, TiledMultiMapRenderer renderer, OrthographicCamera camera) {
    this.engine = engine;

    player = createPlayerEntity();
    engine.addEntity(player);

    PositionSystem positionSystem = new PositionSystem(renderer);
    engine.addSystem(positionSystem);

    SpineRenderSystem spineRenderSystem = new SpineRenderSystem(renderer);
    engine.addSystem(spineRenderSystem);

    updateables.add(new Camera(camera));
  }

  private Entity createPlayerEntity() {
    Entity player = new Entity();
    player.add(new SpineComponent("spines/spineboy/spineboy", "walk", 0.3f));
    player.add(engine.createComponent(DimensionComponent.class));

    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    float x = Settings.START_FRAME_X * Settings.FRAME_PIXELS_X + (w / 2);
    float y = Settings.START_FRAME_Y * Settings.FRAME_PIXELS_Y + (h / 2);

    PositionComponent positionComponent = engine.createComponent(PositionComponent.class);
    positionComponent.x = x;
    positionComponent.y = y;
    player.add(positionComponent);
    return player;
  }

  public static Entity getPlayer() {
    return player;
  }

  public static void add(Updateable agent) { updateables.add(agent); }

  public void update() {
    engine.update(Gdx.graphics.getDeltaTime());

    for (Updateable updateable : updateables) {
      updateable.update();
    }
  }
}
