package com.nima.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Slot;
import com.esotericsoftware.spine.attachments.Attachment;
import com.esotericsoftware.spine.attachments.RegionAttachment;
import com.nima.actors.Spine;

import static com.nima.util.Settings.MPP;

/**
 * Utilities for handling spines.
 */
public class SpineUtil {
  /**
   * Creates the Box2d body for the given spine
   */
  public static Body createSpineBody(World world, Spine spine) {
    Vector2 center = spine.getCenter();

    BodyDef bdef = new BodyDef();
    bdef.type = BodyDef.BodyType.DynamicBody;
    bdef.position.set(center.x * MPP, center.y * MPP);
    Body b = world.createBody(bdef);

    PolygonShape shape = new PolygonShape();
    float scaling = spine.getJsonScaling();
    shape.setAsBox(spine.skeleton.getData().getWidth()*scaling/2 * MPP, spine.skeleton.getData().getHeight()*scaling/2 * MPP);

    FixtureDef fdef = new FixtureDef();
    fdef.isSensor = true;
    fdef.shape = shape;
    b.createFixture(fdef);
    shape.dispose();

    return b;
  }


  /**
   * Returns the center of the spine by search for
   * a slot with the given name and returning it's first position vertice.
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
        //TODO not exact enough
        String name = slot.getData().getName();
        if(slotName.equals(name)) {
          return new Vector2(vertices[0], vertices[1]);
        }
      }
    }
    return null;
  }


//  public static float[] convertSpine2Box2dVertices(float[] vertices) {
//    if(vertices.length % 5 != 0) {
//      throw new UnsupportedOperationException("Vertices have wrong size");
//    }
//
//    List<Float> converted = new ArrayList<>();
//    int index = 0;
//    while(index < vertices.length) {
//      converted.add(vertices[index] * MPP);
//      converted.add(vertices[index + 1] * MPP);
//      index += 5;
//    }
//
//    return ArrayUtils.toPrimitive(converted.toArray(new Float[converted.size()]));
//  }

//  public static float[] convertSpineVertices(float[] vertices) {
//    if(vertices.length % 5 != 0) {
//      throw new UnsupportedOperationException("Vertices have wrong size");
//    }
//
//    List<Float> converted = new ArrayList<>();
//    int index = 0;
//    while(index < vertices.length) {
//      converted.add(vertices[index]);
//      converted.add(vertices[index + 1]);
//      index += 5;
//    }
//
//    return ArrayUtils.toPrimitive(converted.toArray(new Float[converted.size()]));
//  }



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


//  public static List<List<Float>> getSpineVertices(Spine spine) {
//    List<List<Float>> result = new ArrayList<>();
//    boolean premultipliedAlpha = false;
//    Array<Slot> drawOrder = spine.skeleton.getDrawOrder();
//
//    for(int i = 0, n = drawOrder.size; i < n; i++) {
//      Slot slot = drawOrder.get(i);
//      Attachment attachment = slot.getAttachment();
//      if(attachment instanceof RegionAttachment) {
//        RegionAttachment regionAttachment = (RegionAttachment) attachment;
//        float[] vertices = regionAttachment.updateWorldVertices(slot, premultipliedAlpha);
//        float[] floats = PolygonUtil.convertSpine2Box2dVertices(vertices);
//
//        List<Float> floatList = new ArrayList<Float>(floats.length);
//        for (float f : floats) {
//          floatList.add(Float.valueOf(f));
//        }
//        result.add(floatList);
//      }
//    }
//    return result;
//  }


//  public static Body createSpineBody(World world, Spine spine) {
//    float w = Gdx.graphics.getWidth();
//    float h = Gdx.graphics.getHeight();
//
//    float targetX = Settings.START_FRAME_X * Settings.FRAME_PIXELS_X + (w / 2);
//    float targetY = Settings.START_FRAME_Y * Settings.FRAME_PIXELS_Y + (h / 2);
//
//    boolean premultipliedAlpha = false;
//    Array<Slot> drawOrder = spine.skeleton.getDrawOrder();
//
//    BodyDef def = new BodyDef();
//    def.type = BodyDef.BodyType.DynamicBody;
////    def.fixedRotation = false;
//    def.position.set((targetX) * MPP, (targetY) * MPP);
//    Body body = world.createBody(def);
//
//    for(int i = 0, n = drawOrder.size; i < n; i++) {
//      Slot slot = drawOrder.get(i);
//      Attachment attachment = slot.getAttachment();
//      if(attachment instanceof RegionAttachment) {
//        RegionAttachment regionAttachment = (RegionAttachment) attachment;
//        float[] vertices = regionAttachment.updateWorldVertices(slot, premultipliedAlpha);
//        float[] floats = PolygonUtil.convertSpine2Box2dVertices(vertices);
//        if(floats[0] == 0) {
//          return null;
//        }
//
//        PolygonShape shape = new PolygonShape();
//        shape.set(floats);
//        FixtureDef fdef = new FixtureDef();
//        fdef.isSensor = true;
//        fdef.shape = shape;
//        body.createFixture(fdef);
//        shape.dispose();
//      }
//    }

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
//
//    return body;
//  }

}
