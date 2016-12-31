package com.nima.util;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Slot;
import com.esotericsoftware.spine.attachments.Attachment;
import com.esotericsoftware.spine.attachments.RegionAttachment;
import com.nima.actors.Spine;
import com.nima.components.PositionComponent;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Creating polygon and transform them
 * for the given coordinates.
 */
public class PolygonUtil {

  public static Polygon rectangle2Polygon(float w, float h, float x, float y) {
    Polygon polygon = new Polygon(new float[]{x, y, w + x, y, w + x, h + y, x, h + y});
    polygon.setPosition(x, y);
    return polygon;
  }

  public static Polygon clickPolygon(Vector2 clickPoint) {
    float w = 10;
    float h = 10;
    float x = clickPoint.x - 5;
    float y = clickPoint.y - 5;
    float[] vertices = createVertices(w, h, x, y);
    Polygon polygon = new Polygon(vertices);
    polygon.setPosition(0, 0);
    return polygon;
  }

  private static float[] createVertices(float w, float h, float x, float y) {
    return new float[]{x, y, w + x, y, w + x, h + y, x, h + y};
  }

  public static float[] convertSpineVertices(float[] vertices) {
    if(vertices.length % 5 != 0) {
      throw new UnsupportedOperationException("Vertices have wrong size");
    }

    List<Float> converted = new ArrayList<>();
    int index = 0;
    while(index < vertices.length) {
      converted.add(vertices[index]);
      converted.add(vertices[index + 1]);
      index += 5;
    }

    return ArrayUtils.toPrimitive(converted.toArray(new Float[converted.size()]));
  }

  public static boolean checkForCollision(List<Object> sourceEntity, List<Object> targetEntities) {
    for(Object thisCollisionComponent : targetEntities) {
      for(Object thatCollisionComponent : sourceEntity) {
        if(thisCollisionComponent instanceof Polygon && thatCollisionComponent instanceof Polygon) {
          if(Intersector.overlapConvexPolygons((Polygon) thisCollisionComponent, (Polygon) thatCollisionComponent)) {
            return true;
          }
        }
        else if(thisCollisionComponent instanceof Polygon && thatCollisionComponent instanceof Circle) {
          Polygon p = (Polygon) thisCollisionComponent;
          Circle c = (Circle) thatCollisionComponent;
          if(PolygonUtil.circleIntersectingPolygon(c, p)) {
            return true;
          }
        }
        else if(thatCollisionComponent instanceof Polygon && thisCollisionComponent instanceof Circle) {
          Polygon p = (Polygon) thatCollisionComponent;
          Circle c = (Circle) thisCollisionComponent;
          if(PolygonUtil.circleIntersectingPolygon(c, p)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  public static Vector2 getSpineCenter(Spine spine, String slotName) {
    Array<Slot> drawOrder = spine.skeleton.getDrawOrder();
    boolean premultipliedAlpha = false;
    for(int i = 0, n = drawOrder.size; i < n; i++) {
      Slot slot = drawOrder.get(i);
      Attachment attachment = slot.getAttachment();
      if(attachment instanceof RegionAttachment) {
        RegionAttachment regionAttachment = (RegionAttachment) attachment;
        float[] vertices = regionAttachment.updateWorldVertices(slot, premultipliedAlpha);
        String name = slot.getData().getName();
        if(slotName.equals(slotName)) {
          return new Vector2(vertices[0], vertices[1]);
        }
      }
    }
    return null;
  }

//  public static List<Polygon> createSpinePolygons(Spine spine) {
//    List<Polygon> polygons = new ArrayList<>();
//    boolean premultipliedAlpha = false;
//    Array<Slot> drawOrder = spine.skeleton.getDrawOrder();
//    for(int i = 0, n = drawOrder.size; i < n; i++) {
//      Slot slot = drawOrder.get(i);
//      Attachment attachment = slot.getAttachment();
//      if(attachment instanceof RegionAttachment) {
//        RegionAttachment regionAttachment = (RegionAttachment) attachment;
//        float[] vertices = regionAttachment.updateWorldVertices(slot, premultipliedAlpha);
//        String name = slot.getData().getName();
//        Polygon p = new Polygon(PolygonUtil.convertSpineVertices(vertices));
//        polygons.add(p);
//        TiledMultiMapRenderer.debugRenderer.render(name, p);
//      }
//    }
//    return polygons;
//  }

  private static boolean rendered = false;

  public static void createSpineBody(World world, Spine spine) {
    if(rendered) {
      return;
    }

    PositionComponent pos = spine.getComponent(PositionComponent.class);

    boolean premultipliedAlpha = false;
    Array<Slot> drawOrder = spine.skeleton.getDrawOrder();
    for(int i = 0, n = drawOrder.size; i < n; i++) {
      Slot slot = drawOrder.get(i);
      Attachment attachment = slot.getAttachment();
      if(attachment instanceof RegionAttachment) {
        RegionAttachment regionAttachment = (RegionAttachment) attachment;
        float[] vertices = regionAttachment.updateWorldVertices(slot, premultipliedAlpha);
        String name = slot.getData().getName();
        float[] floats = PolygonUtil.convertSpineVertices(vertices);

        if(floats[0] > 0) {
          rendered = true;
          BodyDef def = new BodyDef();
          def.type = BodyDef.BodyType.DynamicBody;
          def.fixedRotation = false;
          def.position.set((floats[0] + pos.x) * Settings.MPP, (floats[1] + pos.y) * Settings.MPP);
          Body body = world.createBody(def);

          PolygonShape shape = new PolygonShape();
          shape.setAsBox((floats[2] - floats[0]) / 2 * Settings.MPP, (floats[3] - floats[5]) / 2 * Settings.MPP);
          body.createFixture(shape, 1f);
          shape.dispose();
        }


      }
    }
  }

  public static boolean circleIntersectingPolygon(Circle c, Polygon p) {
    float[] vertices = p.getTransformedVertices();
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
    return false;
  }

  /**
   * There is no actual center of a polygon, so we simply
   * use the first and the middle vertice and use the distance between them
   */
  public static Vector2 getCenter(Polygon polygon) {
    float[] vertices = polygon.getVertices();
    int index = Math.round(vertices.length / 2);

    Vector2 point1 = new Vector2(vertices[0], vertices[1]);
    Vector2 point2 = new Vector2(vertices[index + 1], vertices[index + 2]);
    float distance = point1.dst(point2);

    return new Vector2(polygon.getX() + distance / 2, polygon.getY() + distance / 2);
  }

  private static Polygon rectangle2Polygon(Rectangle rectangle) {
    float w = rectangle.width;
    float h = rectangle.height;
    float x = 0;
    float y = 0;
    return rectangle2Polygon(w, h, x, y);
  }
}
