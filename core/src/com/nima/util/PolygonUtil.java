package com.nima.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Slot;
import com.esotericsoftware.spine.attachments.Attachment;
import com.esotericsoftware.spine.attachments.RegionAttachment;
import com.nima.actors.Spine;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

import static com.nima.util.Settings.MPP;

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

  public static float[] convertSpine2Box2dVertices(float[] vertices) {
    if(vertices.length % 5 != 0) {
      throw new UnsupportedOperationException("Vertices have wrong size");
    }

    List<Float> converted = new ArrayList<>();
    int index = 0;
    while(index < vertices.length) {
      converted.add(vertices[index] * MPP);
      converted.add(vertices[index + 1] * MPP);
      index += 5;
    }

    return ArrayUtils.toPrimitive(converted.toArray(new Float[converted.size()]));
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

  /**
   * @deprecated use box2d center!
   */
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
        if(slotName.equals(name)) {
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

  public static List<List<Float>> getSpineVertices(Spine spine) {
    List<List<Float>> result = new ArrayList<>();
    boolean premultipliedAlpha = false;
    Array<Slot> drawOrder = spine.skeleton.getDrawOrder();

    for(int i = 0, n = drawOrder.size; i < n; i++) {
      Slot slot = drawOrder.get(i);
      Attachment attachment = slot.getAttachment();
      if(attachment instanceof RegionAttachment) {
        RegionAttachment regionAttachment = (RegionAttachment) attachment;
        float[] vertices = regionAttachment.updateWorldVertices(slot, premultipliedAlpha);
        float[] floats = PolygonUtil.convertSpine2Box2dVertices(vertices);

        List<Float> floatList = new ArrayList<Float>(floats.length);
        for (float f : floats) {
          floatList.add(Float.valueOf(f));
        }
        result.add(floatList);
      }
    }
    return result;
  }

  public static Body createSpineBody(World world, Spine spine) {
    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    float targetX = Settings.START_FRAME_X * Settings.FRAME_PIXELS_X + (w / 2);
    float targetY = Settings.START_FRAME_Y * Settings.FRAME_PIXELS_Y + (h / 2);

    boolean premultipliedAlpha = false;
    Array<Slot> drawOrder = spine.skeleton.getDrawOrder();

    BodyDef def = new BodyDef();
    def.type = BodyDef.BodyType.DynamicBody;
//    def.fixedRotation = false;
    def.position.set((targetX) * MPP, (targetY) * MPP);
    Body body = world.createBody(def);

    for(int i = 0, n = drawOrder.size; i < n; i++) {
      Slot slot = drawOrder.get(i);
      Attachment attachment = slot.getAttachment();
      if(attachment instanceof RegionAttachment) {
        RegionAttachment regionAttachment = (RegionAttachment) attachment;
        float[] vertices = regionAttachment.updateWorldVertices(slot, premultipliedAlpha);
        float[] floats = PolygonUtil.convertSpine2Box2dVertices(vertices);
        if(floats[0] == 0) {
          return null;
        }

        PolygonShape shape = new PolygonShape();
        shape.set(floats);
        FixtureDef fdef = new FixtureDef();
        fdef.isSensor = true;
        fdef.shape = shape;
        body.createFixture(fdef);
        shape.dispose();
      }
    }

//    BodyDef bdef = new BodyDef();
//    bdef.type = BodyDef.BodyType.StaticBody;
//    bdef.position.set(targetX*MPP, targetY*MPP);
//    Body b = world.createBody(def);
//    PolygonShape shape = new PolygonShape();
//    shape.setAsBox(spine.skeleton.getData().getWidth()* MPP/2, spine.skeleton.getData().getHeight()*MPP/2);
//    FixtureDef fdef = new FixtureDef();
//    fdef.isSensor = true;
//    fdef.shape = shape;
//    b.createFixture(fdef);
//    shape.dispose();

    return body;
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
}
