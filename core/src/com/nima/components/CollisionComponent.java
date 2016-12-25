package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Slot;
import com.esotericsoftware.spine.attachments.Attachment;
import com.esotericsoftware.spine.attachments.RegionAttachment;
import com.nima.render.TiledMultiMapRenderer;
import com.nima.util.PolygonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Component responsible for handling the collision of entities
 */
public class CollisionComponent implements Component {
  private List<Polygon> polygons = new ArrayList<>();
  private MapObject mapObject;
  private SpineComponent spineComponent;
  private Circle circle;
  private List<CollisionComponent> collidingObjects = new ArrayList<>();

  public CollisionComponent(SpineComponent spineComponent) {
    this.spineComponent = spineComponent;
  }

  /**
   * Constructor for tiled map objects.
   * The map objects have a fix position, so we can immediately calculate
   * the polygon for it.
   *
   * @param object the map object to create the polygon for
   */
  public CollisionComponent(MapObject object) {
    this.mapObject = object;

    if(mapObject instanceof RectangleMapObject) {
      RectangleMapObject rectangle = (RectangleMapObject) mapObject;
      polygons.add(PolygonUtil.rectangle2Polygon(rectangle.getRectangle()));
      TiledMultiMapRenderer.debugRenderer.render(polygons);
    }
    else if(mapObject instanceof PolygonMapObject) {
      PolygonMapObject p = (PolygonMapObject) mapObject;
      polygons.add(p.getPolygon());
      TiledMultiMapRenderer.debugRenderer.render(polygons);
    }
    else if(mapObject instanceof EllipseMapObject) {
      EllipseMapObject e = (EllipseMapObject) mapObject;
      Ellipse ellipse = e.getEllipse();
      float radius = ellipse.width / 2;
      circle = new Circle(ellipse.x + radius, ellipse.y + radius, radius);
      TiledMultiMapRenderer.debugRenderer.render(circle);
    }
  }

  public void updateBody() {
    if(this.spineComponent != null) {
      polygons.clear();
      boolean premultipliedAlpha = false;
      Array<Slot> drawOrder = spineComponent.skeleton.getDrawOrder();
      for(int i = 0, n = drawOrder.size; i < n; i++) {
        Slot slot = drawOrder.get(i);
        Attachment attachment = slot.getAttachment();
        if(attachment instanceof RegionAttachment) {
          RegionAttachment regionAttachment = (RegionAttachment) attachment;
          float[] vertices = regionAttachment.updateWorldVertices(slot, premultipliedAlpha);
          String name = slot.getData().getName();
          Polygon p = new Polygon(PolygonUtil.convertSpineVertices(vertices));
          polygons.add(p);
          TiledMultiMapRenderer.debugRenderer.render(name, p);
        }
      }
    }
  }

  /**
   * Checks the collision of an entity with the owner
   * of the Dimension Component.
   * We know that his method is always called from a spine or sprite, so
   * the source is always a polygon!
   */
  public boolean collidesWith(Entity entity, CollisionComponent collisionComponent) {
    if(!collisionComponent.polygons.isEmpty()) {
      for(Polygon polygon : polygons) {
        for(Polygon p : collisionComponent.polygons) {
          if(Intersector.overlapConvexPolygons(polygon, p)) {
            return true;
          }
        }
      }
    }
    else if(collisionComponent.circle != null) {
      Circle c = collisionComponent.circle;
      for(Polygon polygon : polygons) {
        float[] vertices = polygon.getTransformedVertices();
        Vector2 center = new Vector2(c.x, c.y);
        float squareRadius = c.radius * c.radius;
        for(int i = 0; i < vertices.length; i += 2) {
          if(i == 0) {
            if(Intersector.intersectSegmentCircle(new Vector2(vertices[vertices.length - 2], vertices[vertices.length - 1]), new Vector2(vertices[i], vertices[i + 1]), center, squareRadius)) {
              return true;
            }
          }
          else {
            if(Intersector.intersectSegmentCircle(new Vector2(vertices[i - 2], vertices[i - 1]), new Vector2(vertices[i], vertices[i + 1]), center, squareRadius)) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  public boolean collidesWith(Polygon clickPoint) {
    if(!polygons.isEmpty()) {
      for(Polygon polygon : polygons) {
        if(Intersector.overlapConvexPolygons(polygon, clickPoint)) {
          return true;
        }
      }
    }
    else if(circle != null) {
      float[] vertices = clickPoint.getTransformedVertices();
      Vector2 center = new Vector2(circle.x, circle.y);
      float squareRadius = circle.radius * circle.radius;
      for(int i = 0; i < vertices.length; i += 2) {
        if(i == 0) {
          if(Intersector.intersectSegmentCircle(new Vector2(vertices[vertices.length - 2], vertices[vertices.length - 1]), new Vector2(vertices[i], vertices[i + 1]), center, squareRadius)) {
            return true;
          }
        }
        else {
          if(Intersector.intersectSegmentCircle(new Vector2(vertices[i - 2], vertices[i - 1]), new Vector2(vertices[i], vertices[i + 1]), center, squareRadius)) {
            return true;
          }

        }
      }
    }
    return false;
  }

  public boolean isColliding(CollisionComponent collisionComponent) {
    return collidingObjects.contains(collisionComponent);
  }

  public void addCollision(CollisionComponent collisionComponent) {
    this.collidingObjects.add(collisionComponent);
  }

  public void removeCollision(CollisionComponent collisionComponent) {
    this.collidingObjects.remove(collisionComponent);
  }
}
