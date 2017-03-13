package com.starsailor.util;

/**
 * Utilities for handling spines.
 */
public class SpineUtil {



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
