package com.nima.actors;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapObject;
import com.nima.components.CollisionComponent;
import com.nima.components.MapObjectComponent;
import com.nima.managers.EntityManager;
import com.nima.render.TiledMultiMapRenderer;
import com.nima.systems.PlayerCollisionSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Takes all map objects and refreshes the Ashley engine with new entities
 * that have a dimension and additional components, depending on the object type.
 */
public class MapObjectToEntityConverter implements Updateable {
  private static final Logger LOG = Logger.getLogger(PlayerCollisionSystem.class.getName());

  private TiledMultiMapRenderer renderer;
  private Engine engine;

  private int lastActorFrameX = -1;
  private int lastActorFrameY = -1;

  private List<Entity> currentEntities = new ArrayList();

  public MapObjectToEntityConverter(Engine engine, TiledMultiMapRenderer renderer) {
    this.renderer = renderer;
    this.engine = engine;
  }

  @Override
  public void update() {
    refreshCollidingEntities();
  }

  private void refreshCollidingEntities() {
    if(lastActorFrameX != renderer.actorFrameX || lastActorFrameY != renderer.actorFrameY) {
      this.lastActorFrameX = renderer.actorFrameX;
      this.lastActorFrameY = renderer.actorFrameY;

      List<MapObject> mapObjects = this.renderer.getMapObjects();
      LOG.info("MapObjectToEntityConverter detected " + mapObjects.size() + " entities");

      EntityManager.getInstance().destroy(currentEntities);
      currentEntities.clear();

      for(MapObject mapObject : mapObjects) {
        Entity entity = new Entity();
//        entity.add(new PositionComponent(mapObject));
//        entity.add(new DimensionComponent(mapObject));
        entity.add(new MapObjectComponent(mapObject));
        entity.add(new CollisionComponent(mapObject));

        currentEntities.add(entity);
        EntityManager.getInstance().add(entity);
      }
    }
  }
}
